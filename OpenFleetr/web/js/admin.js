var expanded = false;
var jurisdictionsMap;
var hospitalsMap
$(document).ready(main);

function main(){
    $('#hospitalsTab').click(drawHospitalsMap);
    $('#jurisdictionsTab').click(drawJurisdictionsMap);

}
function showCheckboxes() {
    var checkboxes = document.getElementById("checkboxes");
    if (!expanded) {
        checkboxes.style.display = "block";
        expanded = true;
    } else {
        checkboxes.style.display = "none";
        expanded = false;
    }
}

function drawJurisdictionsMap() {
    jurisdictionsMap = L.map('jurisdictionsMap',{zoomControl:false}).setView([31.7683, 35.2137], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(jurisdictionsMap);
    
}

function drawHospitalsMap() {
    hospitalsMap = L.map('hospitalsMap',{zoomControl:false}).setView([31.7683, 35.2137], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(hospitalsMap);
    
}/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


