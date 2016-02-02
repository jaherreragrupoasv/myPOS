'use strict';

angular.module('myappApp')
    .controller('DiscountSaleDetailController', function ($scope, $rootScope, $stateParams, entity, DiscountSale) {
        $scope.discountSale = entity;
        $scope.load = function (id) {
            DiscountSale.get({id: id}, function(result) {
                $scope.discountSale = result;
            });
        };
        var unsubscribe = $rootScope.$on('myappApp:discountSaleUpdate', function(event, result) {
            $scope.discountSale = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
