'use strict';

angular.module('myappApp')
    .controller('DiscountSaleLineDetailController', function ($scope, $rootScope, $stateParams, entity, DiscountSaleLine) {
        $scope.discountSaleLine = entity;
        $scope.load = function (id) {
            DiscountSaleLine.get({id: id}, function(result) {
                $scope.discountSaleLine = result;
            });
        };
        var unsubscribe = $rootScope.$on('myappApp:discountSaleLineUpdate', function(event, result) {
            $scope.discountSaleLine = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
