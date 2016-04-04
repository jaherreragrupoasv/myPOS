'use strict';

angular.module('myappApp')
    .controller('ArticleDetailController', function ($scope, $rootScope, $stateParams, entity, Article) {
        $scope.article = entity;
        $scope.load = function (id) {
            Article.get({id: id}, function(result) {
                $scope.article = result;
            });
        };
        var unsubscribe = $rootScope.$on('myappApp:articleUpdate', function(event, result) {
            $scope.article = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
