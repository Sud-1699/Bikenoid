<?php
require 'firebaseLib.php';
$deviceId = $_GET['deviceid'];
$latitude = $_GET['lat'];
$longitude= $_GET['lon'];
$speed= $_GET['spd'];
//$Uid= $_POST[];
 

const DEFAULT_URL = 'https://bikenoid.firebaseio.com/';
const DEFAULT_TOKEN = 'jtfhe1wMSjRPpzF8ZsWACKiyaN2OwtU18YnnMGhy';
$DEFAULT_PATH = '/deviceId';

date_default_timezone_set('Asia/Kolkata');

$_devicestatus= array(
// 'device_Id' => $deviceId,
'latitude' => $latitude + 0.0,
'longitude' => $longitude + 0.0,
//'battery' => '100',
'lastUpdated' => date('Y-m-d H:i:s')
);

 

$firebase = new \firebaseLib(DEFAULT_URL, DEFAULT_TOKEN);
$firebase->update($DEFAULT_PATH, $_devicestatus); // updates data in Firebase

print('POST_SUCCESSFUL');
?>