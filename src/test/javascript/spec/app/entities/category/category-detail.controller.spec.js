'use strict';

describe('Category Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCategory, MockArticle;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCategory = jasmine.createSpy('MockCategory');
        MockArticle = jasmine.createSpy('MockArticle');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Category': MockCategory,
            'Article': MockArticle
        };
        createController = function() {
            $injector.get('$controller')("CategoryDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'myappApp:categoryUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
