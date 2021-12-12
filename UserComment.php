<?php

include("connection.php");

$response = array();

if (isset($_POST["function"])) {
    $func = $_POST["function"];
    if ($func == "addComment"){
        addComment($conn);
    }
    else if ($func == "getComment"){
        getComment($conn);
    }
} 
else {
    $response["code"] = -1;
    $response["message"] = "No function data found";        
    echo json_encode($response);
}

function addComment($conn){
    $username = $_POST["username"];
    $movie_id = $_POST["movie_id"];
	$comment = $_POST["comment"];

    $q = "INSERT INTO user_comment(username, movie_id, comment) VALUES('$username', '$movie_id', '$comment')";
    $result = $conn->query($q);
    if ($result){
        $response["message"] = "comment sent";
    }
    else{
        $response["message"] = "request failed";
    }
    echo json_encode($response);
}

function getComment($conn){
    $movie_id = $_POST["movie_id"];

    $q = "SELECT username, comment FROM user_comment where movie_id='$movie_id'";
    $result = $conn->query($q);
    if ($result == null){
        $response["code"] = 404;
    }
    else{
        $response["code"] = 200;
		$username = array();
        $comment = array();
        while($row = mysqli_fetch_array($result)) {
            $username[] = $row['username'];
			$comment[] = $row['comment'];
        }
        $response["username"] = $username;
		$response["comment"] = $comment;
    }
    echo json_encode($response);
}

?>