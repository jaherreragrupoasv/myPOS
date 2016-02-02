'use strict';

angular.module('myappApp')
    .controller('SaleController', function ($scope, $state, Sale) {

        $scope.sales = [];
        $scope.loadAll = function() {
            Sale.query(function(result) {
               $scope.sales = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.sale = {
                fecha: null,
                country: null,
                rate: null,
                subTotal: null,
                discounts: null,
                taxes: null,
                total: null,
                totalPaied: null,
                printDate: null,
                id: null
            };
        };
    });
