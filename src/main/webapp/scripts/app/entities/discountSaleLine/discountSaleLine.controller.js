'use strict';

angular.module('myappApp')
    .controller('DiscountSaleLineController', function ($scope, $state, DiscountSaleLine) {

        $scope.discountSaleLines = [];
        $scope.loadAll = function() {
            DiscountSaleLine.query(function(result) {
               $scope.discountSaleLines = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.discountSaleLine = {
                amount: null,
                id: null
            };
        };
    });
