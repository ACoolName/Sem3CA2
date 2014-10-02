$(document).ready(function () {

    fetchAll();
    deletePerson();
    initPersons();
    initSaveBtn();
    
});


function initSaveBtn() {
    $("#btn_save").click(function () {
        //First create post argument as a JavaScript object
        var fname = $("#fname").val();
        var lname = $("#lname").val();
        var phone = $("#phone").val();
        var email = $("#email").val();
        if (fname === "" || lname === "" || phone === "")
            return;
        var newPerson = {"firstName": fname, "lastName": lname, "phone": phone,
                         "email": email};
        $.ajax({
            url: "../person",
            data: JSON.stringify(newPerson), //Convert newPerson to JSON
            type: "post",
            dataType: 'json',
            error: function (jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText + ": " + textStatus);
            }
        }).done(function (newPerson) {
            $("#id").val(newPerson.id);
            fetchAll();
        });
    });
}

function initPersons() {
    $("#persons").click(function (e) {
        var id = e.target.id;
        if (isNaN(id)) {
            return;
        }
        updateDetails(id);
    });
}

function deletePerson() {
    $("#delete").click(function () {
        $.ajax({
            url: "../person/" + $("#persons option:selected").attr("id"),
            type: "DELETE",
            dataType: 'json',
            error: function (jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText + ": " + textStatus);
            }
        }).done(function () {
            fetchAll();
        });
    });
}

function clearDetails() {
    $("#id").val("");
    $("#fname").val("");
    $("#lname").val("");
    $("#phone").val("");
    $("#email").val("");
}

function updateDetails(id) {
    $.ajax({
        url: "../person/" + id,
        type: "GET",
        dataType: 'json',
        error: function (jqXHR, textStatus, errorThrown) {
            alert(jqXHR.getResonseText + ": " + textStatus);
        }
    }).done(function (person) {
        $("#id").val(person.id);
        $("#fname").val(person.firstName);
        $("#lname").val(person.lastName);
        $("#phone").val(person.phone);
        $("#email").val(person.email);
    });
}

function fetchAll() {
    $.ajax({
        url: "../person",
        type: "GET",
        dataType: 'json',
        error: function (jqXHR, textStatus, errorThrown) {
            alert(textStatus);
        }
    }).done(function (persons) {
        var options = "";
        persons.forEach(function (person) {
            options += "<option id=" + person.id + ">" + person.firstName[0]
                    + ", " + person.lastName + "</option>";
        });
        $("#persons").html(options);
        clearDetails();
    });
}

