/*
 * Copyright (c) 2015 by Rafael Angel Aznar Aparici (rafaaznar at gmail dot com)
 * 
 * openAUSIAS: The stunning micro-library that helps you to develop easily 
 *             AJAX web applications by using Java and jQuery
 * openAUSIAS is distributed under the MIT License (MIT)
 * Sources at https://github.com/rafaelaznar/openAUSIAS
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var listModule = function () {
    var strClass;
    var jsonData;

    var paramsObject;
    var orderParams;
    var filterParams;
    var systemFilterParams;
    var strUrlFromParamsWithoutOrder;
}
listModule.prototype = Object.create(baseModule.prototype);
listModule.prototype.loadThButtons = function (meta, strClase, UrlFromParamsWithoutOrder) {
    return button.getTableHeaderButtons(strClase, meta.Name, UrlFromParamsWithoutOrder);
}
listModule.prototype.loadButtons = function (rowValues, strClass) {
    var botonera = "";
    botonera += button.getTableToobarButton(strClass, 'view', rowValues[0].data, 'glyphicon-eye-open');
    botonera += button.getTableToobarButton(strClass, 'edit', rowValues[0].data, 'glyphicon-pencil');
    botonera += button.getTableToobarButton(strClass, 'delete', rowValues[0].data, 'glyphicon-remove');
    return button.getToolbarBar(botonera);
};
listModule.prototype.loadPopups = function (meta, rowValues, strClase) {
    var botonera = "";

    botonera += "<p><b>(" + rowValues.id + ') ' + strClase + '</b></p>';
    $.each(meta, function (name, metavalue) {
        if (!metavalue.IsObjForeignKey) {

            botonera += '<i>' + metavalue.ShortName + '</i>: ' + ns.strings.printPlainValue(metavalue, rowValues[metavalue.Name], true) + "<br/>"
        }
        if (metavalue.IsObjForeignKey) {

        }
//        if (typeof value === 'string') {
//            botonera += '<i>' + name + '</i>: ' + ns.strings.escapeHtml(value) + "<br/>";
//        } else {
//            botonera += '<i>' + name + '</i>: ' + value + "<br/>";
//        }
    })

    return botonera;
};
listModule.prototype.prepareParams = function () {
    strClass = ausiasFLOW.listModule_class;
    paramsObject = ausiasFLOW.listModule_paramsObject;
    orderParams = parameter.printOrderParamsInUrl(ausiasFLOW.listModule_paramsObject);
    filterParams = parameter.printFilterParamsInUrl(ausiasFLOW.listModule_paramsObject);
    systemFilterParams = parameter.printSystemFilterParamsInUrl(ausiasFLOW.listModule_paramsObject);
    strUrlFromParamsWithoutOrder = parameter.getUrlStringFromParamsObject(parameter.getUrlObjectFromParamsWithoutParamArray(ausiasFLOW.listModule_paramsObject, ["order", "ordervalue"]));
}
listModule.prototype.getHeaderPageTableFunc = function (jsonMeta, strClass, UrlFromParamsWithoutOrder, visibles, acciones) {
    thisObject = this;
    acciones = typeof (acciones) != 'undefined' ? acciones : true;
    arr_meta_data_tableHeader = _.map(jsonMeta, function (oMeta, key) {
        if (oMeta.IsId) {
            return '<th class="col-md-1">'
                    + oMeta.UltraShortName
                    + '<br />'
                    + thisObject.loadThButtons(oMeta, strClass, UrlFromParamsWithoutOrder)
                    + '</th>';
        } else {
            return  '<th>'
                    + oMeta.UltraShortName
                    + '<br />'
                    + thisObject.loadThButtons(oMeta, strClass, UrlFromParamsWithoutOrder)
                    + '</th>';
        }
    });
    //visibles
    if (visibles) {
        arr_meta_data_tableHeader_visibles = arr_meta_data_tableHeader.slice(0, parseInt(visibles));
    } else {
        arr_meta_data_tableHeader_visibles = arr_meta_data_tableHeader;
    }
    if (acciones) {
        arr_meta_data_tableHeader_visibles_acciones = arr_meta_data_tableHeader_visibles.concat(['<th class="col-md-2">Acciones </th>']);
    } else {
        arr_meta_data_tableHeader_visibles_acciones = arr_meta_data_tableHeader_visibles;
    }
    return '<tr>' + arr_meta_data_tableHeader_visibles_acciones.join('') + '</tr>';
}
listModule.prototype.getBodyPageTableFunc = function (meta, page, print_tdValue_function, tdButtons_function, trPopup_function, visibles) {
//thisObject.jsonData.message.page.list: es un array de objetos. Cada objeto contiene una fila de la tabla de la petición
//thisObject.jsonData.message.meta; es un array de objetos. Every object contains metadata from every object to print in every row
    var matrix_meta_data = _.map(page, function (oRow, keyRow) {
        return _.map(meta, function (oMeta, keyMeta) {
            return  {meta: oMeta, data: oRow[oMeta.Name]};
        });
    });
    //is an array (rpp) of arrays (rows) of objects
    //every object contains the data and its metadata
    var arr_meta_data_table_buttons = _.map(matrix_meta_data, function (value, key) {
        return (_.map(matrix_meta_data[key], function (value2, key2) {
            return  '<td>' + print_tdValue_function(value2) + '</td>';
        })
                )
                .slice(0, parseInt(visibles))
                .concat(['<td>' + tdButtons_function(value, strClass) + '</td>']);
    });
    //is an array (rpp) of arrays (rows) of strings
    //every string contains the data of the table cell
    //there's an additional row to contain the buttons for the operations
    var arr_meta_data_table_buttons_reduced = _.map(arr_meta_data_table_buttons, function (value, key) {
        return _.reduce(value, function (memo, num) {
            return memo + num;
        });
    });
    //is an array (rpp) of strings 
    //where every string is a ...
    var str_meta_data_table_buttons_reduced_reduced = _.reduce(arr_meta_data_table_buttons_reduced, function (memo, num) {
        return memo + '<tr>' + num + '</tr>';
    });
    //is a string that contains the table body
    return str_meta_data_table_buttons_reduced_reduced;
}

listModule.prototype.initialize = function () {
    var thisObject = this;
    //**** prepare params
    //paramsObject = ns.param.defaultizeUrlObjectParametersForLists(ns.param.getUrlObjectFromUrlString(this.url));
    this.prepareParams();
    //****
};
listModule.prototype.getPromise = function () {
    if (paramsObject) {
        return promise.getAll(ausiasFLOW.listModule_class, filterParams, orderParams, systemFilterParams);
    }
}
listModule.prototype.getData = function (jsonDataReturned) {
    if (jsonDataReturned) {
        if (jsonDataReturned.status == "200") {
            jsonData = jsonDataReturned;
        } else {
            //informar error
        }
    } else {
        //informar error
    }

};
listModule.prototype.render = function () {
    var paramsObject = ausiasFLOW.listModule_paramsObject;
    if (jsonData.status == 200) {
        var visibles = 6;
        var strTable = table.getTable(
                this.getHeaderPageTableFunc(jsonData.message.meta.message, ausiasFLOW.listModule_class, strUrlFromParamsWithoutOrder, visibles),
                this.getBodyPageTableFunc(jsonData.message.meta.message, jsonData.message.page.message, html.printPrincipal, this.loadButtons, this.loadPopups, visibles)
                );
        return '<div id="tablePlace">' + strTable + '</div>';
    }
};