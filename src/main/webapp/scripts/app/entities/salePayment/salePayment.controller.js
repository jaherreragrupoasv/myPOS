'use strict';

angular.module('myappApp')
    .controller('SalePaymentController', function ($scope, $state, SalePayment) {

        $scope.salePayments = [];
        $scope.loadAll = function() {
            SalePayment.query(function(result) {
               $scope.salePayments = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.salePayment = {
                creditCard: null,
                date: null,
                amount: null,
                id: null
            };
        };
    });
