<?php
    // $host = "localhost";
    // $username = "root";
    // $password = "";
    // $dbname = "myservice";

    // $conn = mysqli_connect($host, $username, $password, $dbname);
    // if (!$conn) {
    //     die("Connection Failed: " . mysqli_connect_error());
    // }

    $host = "localhost";
    $username = "root";
    $password = "";
    $dbname = "ProjectMDP";

    $conn = mysqli_connect($host, $username, $password, $dbname);
    if (!$conn) {
        die("Connection Failed: " . mysqli_connect_error());
    }
?>