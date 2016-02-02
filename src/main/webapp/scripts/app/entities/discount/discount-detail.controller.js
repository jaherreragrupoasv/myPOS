'use strict';

angular.module('myappApp')
    .controller('DiscountDetailController', function ($scope, $rootScope, $stateParams, entity, Discount) {
        $scope.discount = entity;
        $scope.load = function (id) {
            Discount.get({id: id}, function(result) {
                $scope.discount = result;
            });
        };
        var unsubscribe = $rootScope.$on('myappApp:discountUpdate', function(event, result) {
            $scope.discount = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
