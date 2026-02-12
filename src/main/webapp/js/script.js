function validateForm() {
    let name = document.forms["regForm"]["name"].value;
    let email = document.forms["regForm"]["email"].value;
    let desc = document.forms["regForm"]["description"].value;

    // Name Validation
    if (name.length < 3) {
        alert("Name must be at least 3 characters.");
        return false;
    }

    // Strict Email Regex
    let emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    if (!emailPattern.test(email)) {
        alert("Please enter a valid email address.");
        return false;
    }

    // Description Length Check
    if (desc.length < 10) {
        alert("Please provide a more detailed description (at least 10 chars).");
        return false;
    }
    return true;
}