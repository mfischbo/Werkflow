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
	
	JobService.getJobById($routeParams.id).then(function(job) {
		$scope.job = job;
	});
	
	$scope.dropElement = function($event, $data) {
		for (var p in $scope.job.plugins) {
			if ($scope.job.plugins[p].id == $data.id) {
				$scope.job.plugins[p].x = $event.originalEvent.offsetX;
				$scope.job.plugins[p].y = $event.originalEvent.offsetY;
			}
		}
	}
}]);