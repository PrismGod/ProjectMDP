<?php
    include("connection.php");

    $response = array();

    if (isset($_POST["function"])) {
        $func = $_POST["function"];
        if ($func == "logins") {
            logins($conn);       
        }else if ($func == "checkmhs") {
            checkMahasiswa($conn);
        }else if ($func == "addmhs") {
            addMahasiswa($conn);
        }else if ($func == "getallmhs") {
            getAllMahasiswa($conn);
        }else if ($func == "banmhs") {
            banMahasiswa($conn);
        }else if ($func == "unbanmhs") {
            unbanMahasiswa($conn);
        }
    } else {
        $response["code"] = -1;
        $response["message"] = "No function data found";        
        echo json_encode($response);
    }

    function logins($conn) {
        if (isset($_POST["username"]) && isset($_POST["password"])) {
            $username = $_POST["username"];
            $password = $_POST["password"];
            if ($username) {
                if ($username == "admin" && $password == "admin") {
                    $response["code"] = 13;
                    $response["message"] = "Login Successful As Admin";
                } else {
                    $sql_query = "SELECT * FROM users WHERE username = '$username' AND password = '$password' AND banned = 0";
                    $result = mysqli_query($conn, $sql_query);
                    $sql_query2 = "SELECT * FROM users WHERE username = '$username' AND password = '$password' AND banned = 1";
                    $result2 = mysqli_query($conn, $sql_query2);
                    if (mysqli_num_rows($result) > 0) {
                        $response["code"] = 1;
                        $response["message"] = "Login Successful As User";
                    }
                    else if (mysqli_num_rows($result2) > 0) {
                        $response["code"] = -4;
                        $response["message"] = "User Is banned please contact admin";
                    }
                    else {
                        $response["code"] = -3;
                        $response["message"] = "Invalid Username or Password";
                    }
                }
            } else {
                $response["code"] = -3;
                $response["message"] = "Invalid Username or Password";
            }        
        } else {
            $response["code"] = -2;
            $response["message"] = "Invalid Data";
        }
        echo json_encode($response);
    }

    function checkMahasiswa($conn) {
        if (isset($_POST["username"])) {
            $username = $_POST["username"];
            if ($username) {
                    $sql_query = "SELECT * FROM users WHERE username = '$username'";
                    $result = mysqli_query($conn, $sql_query);
                    if (mysqli_num_rows($result) > 0) {
                        $response["code"] = 1;
                        $response["message"] = "User Allready Exist";
                    }
                    else {
                        $response["code"] = -1;
                        $response["message"] = "No user found";
                    }
            } else {
                $response["code"] = -3;
                $response["message"] = "Invalid Username";
            }        
        } else {
            $response["code"] = -2;
            $response["message"] = "Invalid Data";
        }
        echo json_encode($response);
    }

    function addMahasiswa($conn) {
        if (isset($_POST["username"]) && isset($_POST["password"]) ) {
            $username = $_POST["username"];
            $password = $_POST["password"];
            $sql_insert = "INSERT INTO `users` (`username`, `password`, `banned`) VALUES ('$username', '$password', '0')";
            $query = mysqli_query($conn, $sql_insert);
            if ($query) {
                $response["code"] = 1;
                $response["message"] = "Data Inserted!";
            } else {
                $response["code"] = -3;
                $response["message"] = "User Allready Exist";
            }
        } else {
            $response["code"] = -2;
            $response["message"] = "Invalid Data";
        }
        echo json_encode($response);
    }

    function getAllMahasiswa($conn) {
        $sql_query = "SELECT * FROM `users`";
        $result = mysqli_query($conn, $sql_query);
        if (mysqli_num_rows($result) > 0) {
            $data = array();
            $arrmhs = array();
            $ctr = 0;
            while($row = mysqli_fetch_array($result)) {
                $data["username"] = $row["username"];
                $data["password"] = $row["password"];
                $data["banned"] = $row["banned"];
                $arrmhs[$ctr] = $data;
                $ctr++;
            }
            mysqli_free_result($result);
            $response["code"] = 1;
            $response["message"] = "Get Data Successful";
            $response["datamhs"] = $arrmhs;
        } else {
            $response["code"] = -3;
            $response["message"] = "No Data";
        }
        echo json_encode($response);
    }

    function banMahasiswa($conn) {
        $username = $_POST["username"];
        $usernamesubstr = substr("$username",11);
        $sql_query = "UPDATE `users` SET `banned` = '1' WHERE `username` = '$usernamesubstr' ";
        $query = mysqli_query($conn, $sql_query );
        if ($query) {
            $response["code"] = 1;
            $response["message"] = "$username User Unbanned!";
        } else {
            $response["code"] = -3;
            $response["message"] = "$username User Unban Failed!";
        }
        echo json_encode($response);
    }

    function unbanMahasiswa($conn) {
        $username = $_POST["u"];
        $usernamesubstr = substr("$username",11);
        // $sql_query = "UPDATE `users` SET `banned` = '0' WHERE `username` = '$username' ";


        $sql_query = "UPDATE `users` SET `banned` = '0' WHERE `username` = '$usernamesubstr' ";
        $query = mysqli_query($conn, $sql_query );
        if ($query) {
            $response["code"] = 1;
            $response["message"] = "$username User Unbanned!";
        } else {
            $response["code"] = -3;
            $response["message"] = "$username User Unban Failed!";
        }

        echo json_encode($response);
    }
?>