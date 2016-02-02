'use strict';

angular.module('myappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('discountSaleLine', {
                parent: 'entity',
                url: '/discountSaleLines',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myappApp.discountSaleLine.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/discountSaleLine/discountSaleLines.html',
                        controller: 'DiscountSaleLineController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('discountSaleLine');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('discountSaleLine.detail', {
                parent: 'entity',
                url: '/discountSaleLine/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myappApp.discountSaleLine.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/discountSaleLine/discountSaleLine-detail.html',
                        controller: 'DiscountSaleLineDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('discountSaleLine');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'DiscountSaleLine', function($stateParams, DiscountSaleLine) {
                        return DiscountSaleLine.get({id : $stateParams.id});
                    }]
                }
            })
            .state('discountSaleLine.new', {
                parent: 'discountSaleLine',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/discountSaleLine/discountSaleLine-dialog.html',
                        controller: 'DiscountSaleLineDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    amount: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('discountSaleLine', null, { reload: true });
                    }, function() {
                        $state.go('discountSaleLine');
                    })
                }]
            })
            .state('discountSaleLine.edit', {
                parent: 'discountSaleLine',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/discountSaleLine/discountSaleLine-dialog.html',
                        controller: 'DiscountSaleLineDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['DiscountSaleLine', function(DiscountSaleLine) {
                                return DiscountSaleLine.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('discountSaleLine', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('discountSaleLine.delete', {
                parent: 'discountSaleLine',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/discountSaleLine/discountSaleLine-delete-dialog.html',
                        controller: 'DiscountSaleLineDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['DiscountSaleLine', function(DiscountSaleLine) {
                                return DiscountSaleLine.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('discountSaleLine', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
