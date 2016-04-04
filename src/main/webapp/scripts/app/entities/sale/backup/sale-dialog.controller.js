'use strict';

angular.module('myappApp').controller('SaleDialogController',
    ['$scope', '$stateParams', 'Sale', 'SaleLine', 'Article','$http',
        function ($scope, $stateParams, Sale, SaleLine, Article, $http) {

            $scope.sale = {
                country: "ES",
                id:null,
                saleLines: []
            };

            $scope.articles = Article.query();

            $scope.load = function (id) {
                Sale.get({id: id}, function (result) {
                    $scope.sale = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('myappApp:saleUpdate', result);

                $scope.sale = result;

                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.saveLine = function () {

                //$scope.saleLine = {
                //    article:null,
                //    quantity: null,
                //    price: null,
                //    tax: null
                //};

                $scope.sale.saleLines.push($scope.saleLine);
                $scope.clear();

                //var deferred = $q.defer();

                $http
                    .get("http://apilayer.net/api/live?access_key=0c38dc72256724ec14cd4cbaebba940b", {dataType: 'jsonp'})
                    .success(function (data) {

                        console.log(data);
                        //deferred.resolve(data);
                    })
                    .error(function (data) {
                        console.log(data);
                        //deferred.resolve([]);
                    });

                //return deferred.promise;

                $('#field_article').focus();

            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.sale.id != null) {
                    Sale.update($scope.sale, onSaveSuccess, onSaveError);
                } else {
                    Sale.save($scope.sale, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                $scope.saleLine = {
                    quantity: null,
                    price: null,
                    tax: null,
                    id: null,
                    article: null,
                    sale_id: null
                };
            };
        }]);
