WFApp.jobs = angular.module('JobModule', ['ngRoute', 'ang-drag-drop']);
WFApp.jobs.config(['$routeProvider', function($routeProvider) {

	$routeProvider
		.when('/jobs', {
			templateUrl : 'ui-jobs/views/index.html',
			controller  : 'JobIndexController'
		})
		.when('/jobs/:id/edit', {
			templateUrl : 'ui-jobs/views/editor.html',
			controller	: 'JobEditController'
		});
}]);

WFApp.jobs.service("JobService", ["$q", "$http", function($q, $http) {

	return {
		getAllJobs : function() {
			return $http.get("/api/jobs").then(function(page) {
				return page.data.content;
			});
		},
		
		getJobById : function(id) {
			return $http.get("/api/jobs/" + id).then(function(response) {
				return response.data;
			});
		},
		
		createJob : function(job) {
			return $http.post("/api/jobs", job).then(function(response) {
				return response.data;
			});
		},
		
		updateJob : function(job) {
			return $http.patch("/api/jobs/" + job.id, job).then(function(response) {
				return response.data;
			});
		},
		
		deleteJob : function(job) {
			return $http.delete('/api/jobs/' + job.id).then(function(resonse) {
			});
		},
		
		createInstance : function(job) {
			return $http.post('/api/jobs/'+job.id+'/instance').then(function(response) {
				return response.data;
			})
		}
	};
}]);

WFApp.jobs.controller("JobIndexController", ['$scope', 'JobService', function($scope, JobService) {
	JobService.getAllJobs().then(function(jobs) {
		$scope.jobs = jobs;
	});
	
	$scope.createInstance = function(job) {
		JobService.createInstance(job);
	}
}]);

WFApp.jobs.controller('JobEditController', ['$scope', '$routeParams', 'JobService', function($scope, $routeParams, JobService) {
	
	$scope.init = function() {
		JobService.getJobById($routeParams.id).then(function(job) {
			$scope.job = job;
		});
	};
	
	$scope.addConnection = function(sourceId, sourcePort, targetId, targetPort) {
		for (var i in $scope.job.plugins) {
			if ($scope.job.plugins[i].id == sourceId) {
				var p = $scope.job.plugins[i];
				
				p.connections.push({
					sourcePort : sourcePort,
					targetPort : targetPort,
					targetPluginId : targetId
				});
				console.log($scope.job);
			}
		}
	};
}]);

WFApp.jobs.directive('plugIn', function() {
	return {
		link : function(scope, element, attrs) {
			
			jsPlumb.makeTarget(element, {
			});
			jsPlumb.draggable(element, {
				containment: 'parent'
			});
		}
	};
});

var Endpoints = [];

WFApp.jobs.directive('plugInput', function() {
	return {
		link : function(scope, element, attrs) {
			jsPlumb.addEndpoint($(element).parent(), {
				isSource 		: false,
				maxConnections 	: 1,
				isTarget 		: true,
				anchor  		: 'LeftMiddle',
				parameters      : {
					type : attrs.type,
					port : attrs.port
				}
			});
		}
	};
});

WFApp.jobs.directive('plugOutput', function() {
	return {
		replace : true,
		link    : function(scope, element, attrs) {
		
			jsPlumb.addEndpoint($(element).parent(), {
				maxConnections : 1,
				isSource : true,
				isTarget : false,
				anchor   : 'RightMiddle',
				parameters : {
					type : attrs.type,
					port : attrs.port
				}
			});
		}
	};
});

WFApp.jobs.directive('connections', function() {
	return {
		replace : true,
		template : ''
	};
});

WFApp.jobs.directive('connection', function() {
	return {
		link : function(scope, element, attrs) {

			var src, dst = undefined;
			jsPlumb.selectEndpoints().each(function(ep) {
				if (ep.getElement().id == attrs.from && ep.getParameter('port') == attrs.sourcePort && ep.isSource)
					src = ep;
				if (ep.getElement().id == attrs.to && ep.getParameter('port') == attrs.targetPort && !ep.isSource)
					dst = ep;
			});	

			jsPlumb.connect({
				source : src,
				target : dst 
			});
		}
	};
});


WFApp.jobs.directive('droppable', function($compile) {
	return {
		restrict : 'A',
		controller: 'JobEditController',
		link : function(scope, element, attrs) {
		
			scope.init();
			
			element.droppable({
				drop : function(event, ui) {
					var dIndex = angular.element(ui.draggable).data('identifier'),
					dragEl = angular.element(ui.draggable),
					dropEl = angular.element(this);
					
					if (dragEl.hasClass('menu-item') && dropEl.hasClass('drop-container')) {
						var x = event.pageX - 300;
						var y = event.pageY - 300;
						scope.addModuleToSchema(dIndex, x, y);
					}
					scope.$apply();
				}
			});
			
			jsPlumb.bind('connection', function(info, origEvent) {
				// origEvent is only defined on connections created by user input
				// so we use this as indicator for new connections not created programatically
				if (origEvent) {
					console.log(info);
					scope.addConnection(info.sourceId, info.sourceEndpoint.getParameter('port'), 
							info.targetId, info.targetEndpoint.getParameter('port'));
				}
			});
		}
	}
});

WFApp.jobs.directive('draggable', function() {
	return {
		restrict : 'A',
		link     : function(scope, element, attrs) {
			element.draggable({
				revert : false
			});
		}
	};
});




