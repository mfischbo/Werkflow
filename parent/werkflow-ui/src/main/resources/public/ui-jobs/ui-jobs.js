WFApp.jobs = angular.module('JobModule', ['ngRoute']);
WFApp.jobs.config(['$routeProvider', function($routeProvider) {

	$routeProvider
		.when('/jobs', {
			templateUrl : 'ui-jobs/views/index.html',
			controller  : 'JobIndexController'
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
			return $http.get("/api/jobs").then(function(response) {
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
		}
	};
}]);

WFApp.jobs.controller("JobIndexController", ['$scope', 'JobService', function($scope, JobService) {
	JobService.getAllJobs().then(function(jobs) {
		$scope.jobs = jobs;
	});
}]);

