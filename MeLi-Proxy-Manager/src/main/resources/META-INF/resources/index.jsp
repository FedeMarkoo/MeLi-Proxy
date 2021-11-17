<%--suppress ALL --%>
<%--suppress ALL --%>
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
%>
<html lang="en">

<HEAD>
    <title>Manager Proxy</title>
    <meta content="text/html; charset=utf-8"/>
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
                <form ng-submit="updateMaxValues()">
                    <label for="rxIp">Request per IP:</label>
                    <input type="text" id="rxIp" ng-model="rxIp"><br>
                    <label for="rxPath">Request per Path:</label>
                    <input type="text" id="rxPath" ng-model="rxPath"><br>
                    <label for="rxCombo">Request per Combo:</label>
                    <input type="text" id="rxCombo" ng-model="rxCombo"><br>
                    <label for="rxUA">Request per UserAgent:</label>
                    <input type="text" id="rxUA" ng-model="rxUA"><br>
                    <input type="submit" value="Submit">
                </form>
            </div>
            <div class="table">
                <form ng-submit="addIpDenied()">
                    <label for="ipDenied">Ip to Block:</label>
                    <input type="text" id="ipDenied" ng-model="ipDenied">
                    <input type="submit" value="Submit">
                </form>
                <table>
                    <tr>
                        <th id="headIp">Ips in Blacklist</th>
                    </tr>
                    <tr ng-repeat="ip in ipBlacklist">
                        <td ng-dblclick="whiteIp(ip)">{{ip}}</td>
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
                        <th id="headUA">UserAgents in Blacklist</th>
                    </tr>
                    <tr ng-repeat="userAgent in userAgentBlacklist">
                        <td ng-dblclick="whiteUserAgent(userAgent)">{{userAgent}}</td>
                    </tr>
                </table>
            </div>
        </div>

        <label for="chartIpMetrics">Ip Metrics:</label>
        <div class="metrics,ip,userAgents">
            <div id="chartIpMetrics"></div>
        </div>

        <label for="chartPathMetrics">Path Metrics:</label>
        <div class="metrics,path,userAgents">
            <div id="chartPathMetrics"></div>
        </div>

        <label for="chartComboMetrics">Combo Metrics:</label>
        <div class="metrics,path,userAgents">
            <div id="chartComboMetrics"></div>
        </div>

        <label for="chartUserAgentMetrics">UserAgent Metrics:</label>
        <div class="metrics,userAgent,userAgents">
            <div id="chartUserAgentMetrics"></div>
        </div>

    </div>
</div>

<script>
    angular.module('meliProxy', ['angular.filter', 'zingchart-angularjs']).controller('ProxyController',
        function ($scope, $http) {
            $scope.getData = function () {
                $http.get('/accessControllerValues').success(function (response) {
                    $scope.rxIp = response["maxRequestPerIp"];
                    $scope.rxPath = response["maxRequestPerPath"];
                    $scope.rxCombo = response["maxRequestPerCombo"];
                    $scope.rxUA = response["maxRequestPerUserAgent"];
                });
                $http.get('/blacklist/ip').success(function (response) {
                    $scope.ipBlacklist = response;
                });
                $http.get('/blacklist/userAgent').success(function (response) {
                    $scope.userAgentBlacklist = response;
                });
                $http.get('/counter/ip').success(function (response) {
                    processResponse(response, "ip", "chartIpMetrics")
                });

                $http.get('/counter/path').success(function (response) {
                    processResponse(response, "path", "chartPathMetrics")
                });

                $http.get('/counter/combo').success(function (response) {
                    processResponse(response, "combo", "chartComboMetrics")
                });

                $http.get('/counter/userAgent').success(function (response) {
                    processResponse(response, "userAgent", "chartUserAgentMetrics")
                });
            }

            $scope.updateMaxValues = function () {
                $.ajax({
                    type: "POST",
                    url: "/accessControllerValues",
                    data: JSON.stringify({
                        "maxRequestPerIp": $scope.rxIp,
                        "maxRequestPerPath": $scope.rxPath,
                        "maxRequestPerCombo": $scope.rxCombo,
                        "maxRequestPerUserAgent": $scope.rxUA
                    }),
                    contentType: 'application/json'
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

            $scope.whiteIp = function (item) {
                $.ajax({
                    url: "/blacklist/ip/" + item,
                    type: 'DELETE',
                    success: function () {
                        const index = $scope.userAgentBlacklist.indexOf(item);
                        $scope.userAgentBlacklist.splice(index, 1);
                    }
                });
            }

            $scope.whiteUserAgent = function (item) {
                $.ajax({
                    url: "/blacklist/userAgent/" + item,
                    type: 'DELETE',
                    success: function () {
                        const index = $scope.userAgentBlacklist.indexOf(item);
                        $scope.userAgentBlacklist.splice(index, 1);
                    }
                });
            }

            function progress(timeleft, timetotal, $element) {
                const progressBarWidth = timeleft * $element.width() / timetotal;
                $element.find('div').animate({width: progressBarWidth}, 500).html(timeleft + " seconds to refresh");
                if (timeleft > 0) {
                    setTimeout(function () {
                        progress(timeleft - 1, timetotal, $element);
                    }, 1000);
                } else {
                    $scope.getData();
                    progress(timetotal, timetotal, $element);
                }
            }

            progress(30, 30, $('#progressBar'));

            function processResponse(response, type, chart) {
                let keys = Object.values(response).map(function (a) {
                    return a[type];
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
                    id: chart,
                    data: chartConfig,
                    height: 400,
                    width: "100%"
                });
            }
        }
    );
</script>
</body>

</html>