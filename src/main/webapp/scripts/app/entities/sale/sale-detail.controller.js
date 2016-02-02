'use strict';

angular.module('myappApp')
    .controller('SaleDetailController', function ($scope, $rootScope, $stateParams, entity, Sale, SaleLine) {
        $scope.sale = entity;
        $scope.load = function (id) {
            Sale.get({id: id}, function(result) {
                $scope.sale = result;
            });
        };
        var unsubscribe = $rootScope.$on('myappApp:saleUpdate', function(event, result) {
            $scope.sale = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
