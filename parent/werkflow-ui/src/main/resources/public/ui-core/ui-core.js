WFApp.core = angular.module("CoreModule", []);

WFApp.core.service("JobService", ["$q", "$http", function($q, $http) {

	return {
		getAllJobs : function() {
			return $http.get("/api/jobs").then(function(page) {
				return page.data.content;
			});
		}
	};
}]);

WFApp.core.controller("CoreController", ['$scope', 'JobService', function($scope, JobService) {
	console.log("Test");
	JobService.getAllJobs().then(function(jobs) {
		$scope.jobs = jobs;
	});
}]);