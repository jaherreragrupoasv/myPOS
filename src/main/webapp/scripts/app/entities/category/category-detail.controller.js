'use strict';

angular.module('myappApp')
    .controller('CategoryDetailController', function ($scope, $rootScope, $stateParams, entity, Category, Article) {
        $scope.category = entity;
        $scope.load = function (id) {
            Category.get({id: id}, function(result) {
                $scope.category = result;
            });
        };
        var unsubscribe = $rootScope.$on('myappApp:categoryUpdate', function(event, result) {
            $scope.category = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
