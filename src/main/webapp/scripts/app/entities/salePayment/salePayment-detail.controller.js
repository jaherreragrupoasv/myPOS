'use strict';

angular.module('myappApp')
    .controller('SalePaymentDetailController', function ($scope, $rootScope, $stateParams, entity, SalePayment) {
        $scope.salePayment = entity;
        $scope.load = function (id) {
            SalePayment.get({id: id}, function(result) {
                $scope.salePayment = result;
            });
        };
        var unsubscribe = $rootScope.$on('myappApp:salePaymentUpdate', function(event, result) {
            $scope.salePayment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
