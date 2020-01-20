function reloadDataTables() {
    $(".data-table-auto-load").each(function() {
        $(this).DataTable();
    });
}

function removeRow(element) {
    element.closest('tr').remove();
}

window.rowName = function(element) {
    return element.closest('tr').find('th').text();
}


$(document).ready(function() {
    reloadDataTables();

    jQuery.fn.extend({
        removeRow: function() {
            removeRow(this[0] == undefined ? $(this) : $(this[0]));
        },
        rowName: function() {
            return window.rowName(this[0] == undefined ? $(this) : $(this[0]));
        }
    });
});