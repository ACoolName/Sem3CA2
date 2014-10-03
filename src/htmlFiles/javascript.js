
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

function initSelect() {
    $('#role').on('change', function(e) {
        var valueSelected = this.value;
        if (valueSelected === "AssistantTeacher") {
            console.log("hide" + valueSelected);
            $('#roleInput').hide();
            $('#roleInput').value = '';
        } else {
            console.log("show" + valueSelected);
            $('#roleInput').show();
        }
    });
}

function initViewRoles() {
    $("#btn_viewRole").click(function(e) {
        console.log($("#btn_viewRole").html());
        if ($("#btn_viewRole").html() === "View Role")
        {
            var personsDocument = document.getElementById("persons");
            var selected = personsDocument.options[personsDocument.selectedIndex];
            var id = extractId(selected.id);
            if (isNaN(id)) {
                return;
            }
            $("#persons").hide();
            $("#roles").show();
            $("#btn_viewRole").html("View Courses");
            updateDetailsRoles(id);
        }
        else if ($("#btn_viewRole").html() === "View Courses")
        {
            $("#roles").hide();
            $("#courses").show();
            $("#btn_viewRole").html("View Persons");
            var personsDocument = document.getElementById("roles");
            var selected = personsDocument.options[personsDocument.selectedIndex];
            var id = selected.id;
            if (isNaN(id)) {
                return;
            }
            updateDetailsCourses(id);
        }
        else if ($("#btn_viewRole").html() === "View Persons")
        {
            $("#courses").hide();
            $("#persons").show();
            $("#btn_viewRole").html("View Role");
        }
    });
}

function updateDetailsCourses(id){
    console.log("Beginning of updateCourses")
    var personsDocument = document.getElementById("roles");
    var reselect = -1;
    var index = 0;
    $.ajax({
        url: "../role/" + id,
        type: "GET",
        dataType: 'json',
        error: function(jqXHR, textStatus, errorThrown) {
            alert(textStatus + " shit " + errorThrown);
        }
    }).done(function(roles) {
        var options = "";
        var selected = personsDocument.options[personsDocument.selectedIndex];
        roles.forEach(function(role) {
            index++;
            console.log("FOR EACH!!" + role.roleName);
            switch (role.roleName) {
                case "Teacher":
                    options += "<option id=role_" + role.id + "> Role Name: " + role.roleName
                            + ", Degree: " + role.degree + "</option>";
                case "Student":
                    options += "<option id=role_" + role.id + "> Role Name: " + role.roleName
                            + ", Semester: " + role.semester + "</option>";
                case "AssistantTeacher":
                    options += "<option id=role_" + role.id + "> Role Name: " + role.roleName + "</option>";
            }
            if (typeof selected !== 'undefined') {
                if (parseInt(role.id) === parseInt(extractId(selected.id))) {
                    reselect = index;
                }
            }
        });
        $("#roles").html(options);
        if (reselect !== -1) {
            personsDocument.selectedIndex = reselect - 1;
        }
    }
    );
}

function updateDetailsRoles(id) {
    console.log("Beginning of updateRoles")
    var personsDocument = document.getElementById("roles");
    var reselect = -1;
    var index = 0;
    $.ajax({
        url: "../role/" + id,
        type: "GET",
        dataType: "json",
        error: function(jqXHR, textStatus, errorThrown) {
            alert(textStatus + " shit " + errorThrown);
        }
    }).done(function(roles) {
        console.log("in the done")
        var options = "";
        var selected = personsDocument.options[personsDocument.selectedIndex];
        roles.forEach(function(role) {
            index++;
            console.log("FOR EACH!!" + role.roleName);
            switch (role.roleName) {
                case "Teacher":
                    console.log("--------------" + role.degree);
                    options += "<option id=role_" + role.id + "> Role Name: " + role.roleName
                            + ", Degree: " + role.degree + "</option>";
                    break;
                case "Student":
                    options += "<option id=role_" + role.id + "> Role Name: " + role.roleName
                            + ", Semester: " + role.semester + "</option>";
                    break;
                case "AssistantTeacher":
                    options += "<option id=role_" + role.id + "> Role Name: " + role.roleName + "</option>";
                    break;
            }
            if (typeof selected !== 'undefined') {
                if (parseInt(role.id) === parseInt(extractId(selected.id))) {
                    reselect = index;
                }
            }
        });
        $("#roles").html(options);
        if (reselect !== -1) {
            personsDocument.selectedIndex = reselect - 1;
        }
    }
    );
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

function initAddRole() {
    $("#btn_addRole").click(function(e) {
        var personsDocument = document.getElementById("persons");
        var selected = personsDocument.options[personsDocument.selectedIndex];
        updateDetails(extractId(selected.id));
        var id = extractId(selected.id);
        console.log("blaaa" + id);
        if ($("#role").val() === "AssistantTeacher") {
            var role = {
                roleName: $("#role").val()
            };
        }
        if ($("#role").val() === "Teacher") {
            var role = {
                roleName: $("#role").val(),
                degree: $("#roleInput").val()
            };
        }
        if ($("#role").val() === "Student") {
            var role = {
                roleName: $("#role").val(),
                semester: $("#roleInput").val()
            };
        }
        $.ajax({
            url: "../role/" + id,
            data: JSON.stringify(role),
            type: "post",
            dataType: 'json',
            error: function(jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText + ": " + textStatus);
            }
        }).done(function() {
            fetchAll();
        });
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
    var personsDocument = document.getElementById("persons");
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
        var selected = personsDocument.options[personsDocument.selectedIndex];
        persons.forEach(function(person) {
            index++;
            options += "<option id=person_" + person.id + "> First Name: " + person.firstName
                    + ", Last Name: " + person.lastName + ", Email: " + person.email + ", Phone: " + person.phone + "</option>";
            if (typeof selected !== 'undefined') {
                if (parseInt(person.id) === parseInt(extractId(selected.id))) {
                    reselect = index;
                }
            }
        });
        $("#persons").html(options);
        if (reselect !== -1) {
            personsDocument.selectedIndex = reselect - 1;
        }
    });
}