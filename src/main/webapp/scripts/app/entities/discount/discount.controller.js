'use strict';

angular.module('myappApp')
    .controller('DiscountController', function ($scope, $state, Discount) {

        $scope.discounts = [];
        $scope.loadAll = function() {
            Discount.query(function(result) {
               $scope.discounts = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.discount = {
                discountName: null,
                fromDate: null,
                toDate: null,
                percentage: null,
                minimunToOneFree: null,
                id: null
            };
        };
    });
