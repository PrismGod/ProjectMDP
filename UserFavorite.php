<?php

include("connection.php");

$response = array();

if (isset($_POST["function"])) {
    $func = $_POST["function"];
    if ($func == "checkFavorite"){
        checkFavorite($conn);
    }
    else if ($func == "addFavorite"){
        addFavorite($conn);
    }
    else if ($func == "removeFavorite"){
        removeFavorite($conn);
    }
    else if ($func == "getFavorite"){
        getFavorite($conn);
    }
} 
else {
    $response["code"] = -1;
    $response["message"] = "No function data found";        
    echo json_encode($response);
}

function checkFavorite($conn){
    $username = $_POST["username"];
    $movie_id = $_POST["movie_id"];

    $q = "SELECT * FROM user_favorite WHERE username='$username' AND movie_id=$movie_id";
    $result = $conn->query($q)->fetch_object();
    if ($result == null){
        $response["addedToFavorite"] = false;
    }
    else{
        $response["addedToFavorite"] = true;
    }
    echo json_encode($response);
}

function addFavorite($conn){
    $username = $_POST["username"];
    $movie_id = $_POST["movie_id"];

    $q = "INSERT INTO user_favorite(username, movie_id) VALUES('$username', $movie_id)";
    $result = $conn->query($q);
    if ($result){
        $response["message"] = "movie added to favorite";
    }
    else{
        $response["message"] = "request failed";
    }
    echo json_encode($response);
}

function removeFavorite($conn){
    $username = $_POST["username"];
    $movie_id = $_POST["movie_id"];

    $q = "DELETE FROM user_favorite WHERE username='$username' AND movie_id=$movie_id";
    $result = $conn->query($q);
    if ($result){
        $response["message"] = "movie removed from favorite";
    }
    else{
        $response["message"] = "request failed";
    }
    echo json_encode($response);
}

function getFavorite($conn){
    $username = $_POST["username"];

    $q = "SELECT movie_id FROM user_favorite where username='$username'";
    $result = $conn->query($q);
    if ($result == null){
        $response["code"] = 404;
    }
    else{
        $response["code"] = 200;
        $favorite = array();
        while($row = mysqli_fetch_array($result)) {
            $favorite[] = $row['movie_id'];
        }
        $response["result"] = $favorite;
    }
    echo json_encode($response);
}

?>