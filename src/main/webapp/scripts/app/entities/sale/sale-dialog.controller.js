'use strict';

angular.module('myappApp').controller('SaleDialogController',
    ['$scope', '$stateParams', 'Sale', 'SaleLine', 'Article', 'SalePayment', '$http', 'Principal', '$state',
        function ($scope, $stateParams, Sale, SaleLine, Article, SalePayment, $http, Principal, $state) {

            var hoy = new Date();
            var digitos = "";

            $scope.handleDigit = function (digito) {
                if (digito == "clean") {
                    digitos = "";
                } else if (digito == "del") {
                    digitos = digitos.substr(0, digitos.length-1);
                } else {
                    digitos = digitos.concat(digito);
                }

                $scope.barCode = digitos;
            };


            // retrieve the logged account information
            Principal.identity(true).then(function (account) {
                // After the login the language will be changed to
                // the language selected by the user during his registration

                $scope.sale.country = account.langKey;
            });

            $scope.article = {};

            $scope.sale = {
                country: null,
                id: null,
                fecha: hoy,
                subTotal: 0,
                discounts: 0,
                taxes: 0,
                totalPaied: 0,
                total: 0,
                saleLines: []
            };

            $scope.articles = Article.query();

            $scope.load = function (id) {
                Sale.get({id: id}, function (result) {
                    $scope.sale = result;
                });
            };

            //Inicio de los paneles
            $('#paymentPanel').hide();
            $('#saleFinalPanel').hide();
            $('#printPanel').hide();

            $scope.leftToPay = 0;

            $scope.$watchGroup(['paymentMethod'], function (newValue, oldValue) {
                if (newValue !== oldValue) {
                    if (newValue == "false") {
                        $('#creditCardPanel').show();
                        $('#field_creditCard').focus();
                    }
                    else {
                        $('#creditCardPanel').hide();
                    }
                }
            });

            $scope.$watchGroup(['barCode'], function (newValue, oldValue) {
                if ((newValue !== oldValue) && ($scope.barCode.length == 4)) {
                    //Load article
                    Article.getByBarCode({barCode: newValue}, function (result) {
                        $scope.clearSaleLine();
                        $scope.saleLine.article = result;
                    });
                }
            });

            //FUNCIONES INTERFACE
            $scope.minusQuantity = function (saleLine) {
                saleLine.quantity = saleLine.quantity - 1;

                if (saleLine.quantity == 0) {
                    $scope.deleteLine(saleLine);
                }
            };

            $scope.plusQuantity = function (saleLine) {
                saleLine.quantity = saleLine.quantity + 1;
            };

            $scope.totalLine = function (saleLine) {
                return saleLine.quantity * saleLine.price;
            };

            $scope.totalSale = function () {
                var total = 0;
                var i = 0;

                if ($scope.sale != undefined) {
                    for (i = 0; i < $scope.sale.saleLines.length; i++) {
                        var saleLine = $scope.sale.saleLines[i];

                        total = total + (saleLine.price * saleLine.quantity);
                    }
                }
                return total;
            };

            //CABECERA
            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.sale.id != null) {
                    Sale.update($scope.sale, onSaveSuccess, onSaveError);
                } else {
                    Sale.save($scope.sale, onSaveSuccess, onSaveError);
                }
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('myappApp:saleUpdate', result);

                $scope.sale = result;
                $scope.sale.salePayments = [];

                $scope.isSaving = false;

                $('#salePanel').hide(1000);

                $('#paymentPanel').show(1000);
                $('#saleFinalPanel').show(1000);

                //$('#efectivo').selected = true;
                $scope.paymentMethod = true;
                $scope.leftToPay = $scope.sale.total;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };


            //SALELINES
            $scope.clearSaleLine = function () {
                $scope.saleLine = {
                    quantity: null,
                    price: null,
                    tax: null,
                    id: null,
                    article: null,
                    sale_id: null
                };
            };

            $scope.deleteLine = function (saleLine) {
                var index = $scope.sale.saleLines.indexOf(saleLine);
                $scope.sale.saleLines.splice(index, 1);
            };

            $scope.saveLine = function () {

                if ($scope.saleLine != undefined) {
                    $scope.saleLine.quantity = 1;

                    $scope.saleLine.price = $scope.saleLine.article.price;
                    $scope.saleLine.tax = $scope.saleLine.article.tax;

                    $scope.sale.saleLines.push($scope.saleLine);
                    $scope.clearSaleLine();
                    $scope.handleDigit("clean");

                    //var deferred = $q.defer();

                    //$http
                    //    .get("http://apilayer.net/api/live?access_key=0c38dc72256724ec14cd4cbaebba940b", {dataType: 'jsonp'})
                    //    .success(function (data) {
                    //
                    //        console.log(data);
                    //        //deferred.resolve(data);
                    //    })
                    //    .error(function (data) {
                    //        console.log(data);
                    //        //deferred.resolve([]);
                    //    });

                    //return deferred.promise;

                    $('#field_article').focus();
                }
                ;
            };

            //PAYMENT LINES
            $scope.clearSalePayment = function () {
                $scope.salePayment = {
                    type: null,
                    creditCard: null,
                    amount: null,
                    sale_id: null
                };
            };

            ////////////////////////////////////////////////////////

            $scope.deleteSalePayment = function (salePayment) {
                SalePayment.delete({id: salePayment.id}, onDeletePaymentSuccess(salePayment), onDeletePaymentError);

                var index = $scope.sale.salePayments.indexOf(salePayment);
                $scope.sale.salePayments.splice(index, 1);
            };

            var onDeletePaymentSuccess = function (result) {
                $scope.$emit('myappApp:salePaymentDelete', result);

                $scope.sale.totalPaied = $scope.sale.totalPaied - result.amount;
                $scope.leftToPay = $scope.leftToPay + result.amount;

                $('#efectivo').focus();

                $scope.isSaving = false;
            };

            var onDeletePaymentError = function (result) {
                $scope.isSaving = false;
            };

            ////////////////////////////////////////////////////////

            $scope.saveSalePayment = function () {
                $scope.isSaving = true;

                if ($scope.paymentMethod == true) {
                    $scope.salePayment.type = "EFECTIVO";
                }
                else {
                    $scope.salePayment.type = "TARJETA";
                }
                ;

                $scope.salePayment.sale_id = $scope.sale.id;

                SalePayment.save($scope.salePayment, onSavePaymentSuccess, onSavePaymentError);
            };

            var onSavePaymentSuccess = function (result) {
                $scope.$emit('myappApp:salePaymentUpdate', result);

                $scope.salePayment = result;
                $scope.sale.totalPaied = $scope.sale.totalPaied + result.amount;
                $scope.leftToPay = $scope.leftToPay - result.amount;

                $scope.sale.salePayments.push($scope.salePayment);
                $scope.clearSalePayment();

                if ($scope.leftToPay < 0) {
                    $('#paymentPanel').hide(1000);
                    $('#printPanel').show(1000);
                }
                else {
                    $('#efectivo').focus();
                }

                $scope.isSaving = false;
            };

            var onSavePaymentError = function (result) {
                $scope.isSaving = false;
            };

            //PRINT
            $scope.printSale = function () {
                $scope.isSaving = true;
                if ($scope.sale.id != null) {
                    Sale.print({id: $scope.sale.id}, onPrintSuccess, onPrintError);
                }
            };

            var onPrintSuccess = function (result) {
                $scope.$emit('myappApp:salePrint', result);
                $state.go('home');
            };

            var onPrintError = function (result) {
                $scope.isSaving = false;
            };


        }]);
