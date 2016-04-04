'use strict';

angular.module('myappApp')
    .controller('SaleDetailController', function ($scope, $rootScope, $stateParams, entity, Sale, $state) {
        $scope.sale = entity;
        $scope.load = function (id) {
            Sale.get({id: id}, function(result) {
                $scope.sale = result;
            });
        };
        var unsubscribe = $rootScope.$on('myappApp:saleUpdate', function(event, result) {
            $scope.sale = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.moneda = "€";

        $scope.totalLine = function (saleLine) {
            return saleLine.quantity * saleLine.price;
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

    });
