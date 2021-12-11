<?php

include("connection.php");

$response = array();

if (isset($_POST["function"])) {
    $func = $_POST["function"];
    if ($func == "checkMovie"){
        checkMovie($conn);
    }
    else if ($func == "addWatchlist"){
        addWatchlist($conn);
    }
    else if ($func == "removeWatchlist"){
        removeWatchlist($conn);
    }
    else if ($func == "getWatchlist"){
        getWatchlist($conn);
    }
} 
else {
    $response["code"] = -1;
    $response["message"] = "No function data found";        
    echo json_encode($response);
}

function checkMovie($conn){
    $username = $_POST["username"];
    $movie_id = $_POST["movie_id"];

    $q = "SELECT * FROM user_watchlist WHERE username='$username' AND movie_id=$movie_id";
    $result = $conn->query($q)->fetch_object();
    if ($result == null){
        $response["addedToWatchlist"] = false;
    }
    else{
        $response["addedToWatchlist"] = true;
    }
    echo json_encode($response);
}

function addWatchlist($conn){
    $username = $_POST["username"];
    $movie_id = $_POST["movie_id"];

    $q = "INSERT INTO user_watchlist(username, movie_id) VALUES('$username', $movie_id)";
    $result = $conn->query($q);
    if ($result){
        $response["message"] = "movie added to watchlist";
    }
    else{
        $response["message"] = "request failed";
    }
    echo json_encode($response);
}

function removeWatchlist($conn){
    $username = $_POST["username"];
    $movie_id = $_POST["movie_id"];

    $q = "DELETE FROM user_watchlist WHERE username='$username' AND movie_id=$movie_id";
    $result = $conn->query($q);
    if ($result){
        $response["message"] = "movie removed from watchlist";
    }
    else{
        $response["message"] = "request failed";
    }
    echo json_encode($response);
}

function getWatchlist($conn){
    $username = $_POST["username"];

    $q = "SELECT movie_id FROM user_watchlist where username='$username'";
    $result = $conn->query($q);
    if ($result == null){
        $response["code"] = 404;
    }
    else{
        $response["code"] = 200;
        $watchlist = array();
        while($row = mysqli_fetch_array($result)) {
            $watchlist[] = $row['movie_id'];
        }
        $response["result"] = $watchlist;
    }
    echo json_encode($response);
}

?>