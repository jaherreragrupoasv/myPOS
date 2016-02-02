'use strict';

angular.module('myappApp')
    .controller('DiscountSaleController', function ($scope, $state, DiscountSale) {

        $scope.discountSales = [];
        $scope.loadAll = function() {
            DiscountSale.query(function(result) {
               $scope.discountSales = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.discountSale = {
                amount: null,
                id: null
            };
        };
    });
