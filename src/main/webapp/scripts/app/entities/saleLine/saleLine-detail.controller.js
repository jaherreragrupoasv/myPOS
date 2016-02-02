'use strict';

angular.module('myappApp')
    .controller('SaleLineDetailController', function ($scope, $rootScope, $stateParams, entity, SaleLine, Sale) {
        $scope.saleLine = entity;
        $scope.load = function (id) {
            SaleLine.get({id: id}, function(result) {
                $scope.saleLine = result;
            });
        };
        var unsubscribe = $rootScope.$on('myappApp:saleLineUpdate', function(event, result) {
            $scope.saleLine = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
