'use strict';

angular.module('myappApp')
    .controller('SaleLineController', function ($scope, $state, SaleLine) {

        $scope.saleLines = [];
        $scope.loadAll = function() {
            SaleLine.query(function(result) {
               $scope.saleLines = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.saleLine = {
                quantity: null,
                price: null,
                tax: null,
                id: null
            };
        };
    });
