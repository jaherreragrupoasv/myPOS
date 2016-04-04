'use strict';

angular.module('myappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('saleLine', {
                parent: 'entity',
                url: '/saleLines',
                data: {
                    authorities: [],
                    //authorities: ['ROLE_USER'],
                    pageTitle: 'myappApp.saleLine.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/saleLine/saleLines.html',
                        controller: 'SaleLineController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('saleLine');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('saleLine.detail', {
                parent: 'entity',
                url: '/saleLine/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myappApp.saleLine.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/saleLine/saleLine-detail.html',
                        controller: 'SaleLineDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('saleLine');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SaleLine', function($stateParams, SaleLine) {
                        return SaleLine.get({id : $stateParams.id});
                    }]
                }
            })
            .state('saleLine.new', {
                parent: 'saleLine',
                url: '/new',
                data: {
                    authorities: [],
                    //authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/saleLine/saleLine-dialog.html',
                        controller: 'SaleLineDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    quantity: null,
                                    price: null,
                                    tax: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('saleLine', null, { reload: true });
                    }, function() {
                        $state.go('saleLine');
                    })
                }]
            })
            .state('saleLine.edit', {
                parent: 'saleLine',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/saleLine/saleLine-dialog.html',
                        controller: 'SaleLineDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SaleLine', function(SaleLine) {
                                return SaleLine.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('saleLine', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('saleLine.delete', {
                parent: 'saleLine',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/saleLine/saleLine-delete-dialog.html',
                        controller: 'SaleLineDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['SaleLine', function(SaleLine) {
                                return SaleLine.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('saleLine', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
