<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
%>
<html>

<HEAD>
    <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
    <META HTTP-EQUIV="Expires" CONTENT="-1">
    <title>Manager Proxy</title>
    <link href="css/Style.css" rel="stylesheet">
    <script src="https://kit.fontawesome.com/a7e4de9cfc.js" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.5/angular.min.js"></script>
    <script src="http://cdn.zingchart.com/zingchart.min.js"></script>
    <script src="http://cdn.zingchart.com/angular/zingchart-angularjs.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/angular-filter/0.5.16/angular-filter.js"></script>
</HEAD>

<body>
<div id="progressBar">
    <div></div>
</div>
<div>
    <div ng-app="meliProxy" ng-controller="ProxyController" ng-init="getData()">
        <div class="tableContainer">
            <div class="table">
                <form ng-submit="addIpDenied()">
                    <label for="ipDenied">Ip to Block:</label>
                    <input type="text" id="ipDenied" ng-model="ipDenied">
                    <input type="submit" value="Submit">
                </form>
                <table>
                    <tr>
                        <th>Ips in Blacklist</th>
                    </tr>
                    <tr ng-repeat="ip in ipBlacklist">
                        <td>{{ip}}</td>
                    </tr>
                </table>
            </div>
            <div class="table">
                <form ng-submit="addUserAgentDenied()">
                    <label for="userAgentDenied">UserAgent to Block:</label>
                    <input type="text" id="userAgentDenied" ng-model="userAgentDenied">
                    <input type="submit" value="Submit">
                </form>
                <table>
                    <tr>
                        <th>UserAgents in Blacklist</th>
                    </tr>
                    <tr ng-repeat="userAgent in userAgentBlacklist">
                        <td ng-dblclick="whiteUserAgent()">{{userAgent}}</td>
                    </tr>
                </table>
            </div>
        </div>

        <div class="metrics,ip,userAgents">
            <div id="chartIpMetrics"></div>
        </div>

        <div class="metrics,path,userAgents">
            <div id="chartPathMetrics"></div>
        </div>

    </div>
</div>
</div>

<script>
    angular.module('meliProxy', ['angular.filter', 'zingchart-angularjs']).controller('ProxyController',
        function ($scope, $http) {
            $scope.getData = function () {
                $http.get('/blacklist/ip').success(function (response) {
                    $scope.ipBlacklist = response;
                });
                $http.get('/blacklist/userAgent').success(function (response) {
                    $scope.userAgentBlacklist = response;
                });
                $http.get('/counter/ip').success(function (response) {
                    let keys = Object.values(response).map(function (a) {
                        return a["ip"];
                    });
                    let request = Object.values(response).map(function (a) {
                        return a["requestCant"] - a["deniedCant"];
                    });
                    let denied = Object.values(response).map(function (a) {
                        return a["deniedCant"];
                    });

                    let chartConfig = {
                        type: 'hbar',
                        "plot": {
                            "bar-width": "50%",
                            "stacked": true,
                        },
                        plotarea: {
                            margin: 'dynamic'
                        },
                        'scale-x': {
                            labels: keys
                        },
                        series: [
                            {values: request},
                            {values: denied}
                        ]
                    };

                    zingchart.render({
                        id: 'chartIpMetrics',
                        data: chartConfig,
                        height: 400,
                        width: "100%"
                    });
                });

                $http.get('/counter/path').success(function (response) {
                    $scope.pathsDenied = response;

                    let keys = Object.values(response).map(function (a) {
                        return a["path"];
                    });
                    let request = Object.values(response).map(function (a) {
                        return a["requestCant"] - a["deniedCant"];
                    });
                    let denied = Object.values(response).map(function (a) {
                        return a["deniedCant"];
                    });

                    let chartConfig = {
                        type: 'hbar',
                        "plot": {
                            "bar-width": "50%",
                            "stacked": true,
                        },
                        plotarea: {
                            margin: 'dynamic'
                        },
                        'scale-x': {
                            labels: keys
                        },
                        series: [
                            {values: request},
                            {values: denied}
                        ]
                    };

                    zingchart.render({
                        id: 'chartPathMetrics',
                        data: chartConfig,
                        height: 400,
                        width: "100%"
                    });
                });
            }

            $scope.addIpDenied = function () {
                $.ajax({
                    url: "/blacklist/ip/" + $scope.ipDenied,
                    type: 'PUT',
                    success: function () {
                        $scope.ipBlacklist.push($scope.ipDenied);
                        $scope.ipDenied = '';
                    }
                });
            }

            $scope.addUserAgentDenied = function () {
                $.ajax({
                    url: "/blacklist/userAgent/" + $scope.userAgentDenied,
                    type: 'PUT',
                    success: function () {
                        $scope.userAgentBlacklist.push($scope.userAgentDenied);
                        $scope.userAgentDenied = '';
                    }
                });
            }

            $scope.whiteIp = function () {
                let item = event.target.textContent;
                $.ajax({
                    url: "/blacklist/ip/" + event.target.textContent,
                    type: 'DELETE',
                    success: function () {
                        var index = $scope.userAgentBlacklist.indexOf(item);
                        $scope.userAgentBlacklist.splice(index, 1);
                    }
                });
            }

            $scope.whiteUserAgent = function () {
                let item = event.target.textContent;
                $.ajax({
                    url: "/blacklist/userAgent/" + item,
                    type: 'DELETE',
                    success: function () {
                        var index = $scope.userAgentBlacklist.indexOf(item);
                        $scope.userAgentBlacklist.splice(index, 1);
                    }
                });
            }

            function progress(timeleft, timetotal, $element) {
                var progressBarWidth = timeleft * $element.width() / timetotal;
                $element.find('div').animate({width: progressBarWidth}, 500).html(timeleft + " seconds to refresh");
                if (timeleft > 0) {
                    setTimeout(function () {
                        progress(timeleft - 1, timetotal, $element);
                    }, 1000);
                } else {
                    $scope.getData();
                    progress(30, 30, $element);
                }
            };

            progress(30, 30, $('#progressBar'));
        }
    );
</script>
</body>

</html>