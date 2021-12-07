var app = angular.module("REF",[]);

app.controller("REF_CONTROLLER",function($scope, $http) {
    $scope.refusalAll = []

    $scope.init = function () {
        var url = '/sign/document';
        $http({
            url: url,
            method: 'GET',
            params:{
                sign_flag: false,
            },
        }).then(function (response) {
            console.log(response.data)
            $scope.refusalAll = response.data;
        })
    }

    // $scope.incomingDocument = function (incoming) {
    //     var url = '/document/incoming';
    //     $http({
    //         url: url,
    //         method: 'GET',
    //         params:{
    //             document_name: incoming.document_name,
    //         },
    //         responseType:'arraybuffer'
    //     }).then(function (response) {
    //         var file = new Blob([response.data], { type: 'application/pdf' });
    //         var fileURL = URL.createObjectURL(file);
    //
    //         window.open(fileURL);
    //     })
    // }

    // $scope.sentDocumentAndSign = function (incoming) {
    //     console.log("sign")
    //     var url = '/sign/contract';
    //     $http({
    //         url: url,
    //         method: 'POST',
    //         data: {
    //             document_name: incoming.document_name,
    //             email_receiver: incoming.email_sender
    //         },
    //     });
    // }
    //
    // $scope.sentDocumentAndNoSing = function (incoming) {
    //     var url = '/sign/no/contract';
    //     $http({
    //         url: url,
    //         method: 'POST',
    //         data: {
    //             document_name: incoming.document_name,
    //             email_receiver: incoming.email_sender
    //         },
    //     });
    // }
});
