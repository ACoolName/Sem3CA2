


function initSaveBtn() {
    $("#btn_add").click(function() {
        //First create post argument as a JavaScript object
        var fname = $("#fname").val();
        var lname = $("#lname").val();
        var phone = $("#phone").val();
        var email = $("#email").val();
        if (fname === "" || lname === "" || phone === "" || email === "")
            return;
        var newPerson = {"firstName": fname, "lastName": lname, "phone": phone,
            "email": email};
        $.ajax({
            url: "../person",
            data: JSON.stringify(newPerson), //Convert newPerson to JSON
            type: "post",
            dataType: 'json',
            error: function(jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText + ": " + textStatus);
            }
        }).done(function(newPerson) {
            $("#id").val(newPerson.id);
            fetchAll();
        });
    });
}

function initPersons() {
    $("#persons").click(function(e) {
        var id = e.target.id;
        if (isNaN(id)) {
            return;
        }
        updateDetails(id);
    });
}

function extractId(line) {
    return line.split("_")[1];
}

function deletePerson() {
    $("#delete").click(function() {
        $.ajax({
            url: "../person/" + extractId($("#persons option:selected").attr("id")),
            type: "DELETE",
            dataType: 'json',
            error: function(jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText + ": " + textStatus);
            }
        }).done(function() {
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
        error: function(jqXHR, textStatus, errorThrown) {
            alert(jqXHR.getResonseText + ": " + textStatus);
        }
    }).done(function(person) {
        $("#id").val(person.id);
        $("#fname").val(person.firstName);
        $("#lname").val(person.lastName);
        $("#phone").val(person.phone);
        $("#email").val(person.email);
    });
}

function fetchAll() {
    var selectedItem = document.getElementById("persons");
    var reselect = -1;
    var index = 0;
    $.ajax({
        url: "../person",
        type: "GET",
        dataType: 'json',
        error: function(jqXHR, textStatus, errorThrown) {
            alert(textStatus + " shit " + errorThrown);
        }
    }).done(function(persons) {
        var options = "";
        var sel = selectedItem.options[selectedItem.selectedIndex];
        persons.forEach(function(person) {
            index++;
            options += "<option id=person_" + person.id + "> First Name: " + person.firstName
                    + ", Last Name: " + person.lastName + ", Email: " + person.email + ", Phone: " + person.phone + "</option>";
            if (typeof sel !== 'undefined') {
                if (parseInt(person.id) === parseInt(extractId(sel.id))) {
                    reselect = index;
                }
            }
        });
        $("#persons").html(options);
        if (reselect !== -1) {
            selectedItem.selectedIndex = reselect - 1;
        }
    });
}



