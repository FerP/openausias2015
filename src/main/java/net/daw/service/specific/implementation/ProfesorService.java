/*
 * Copyright (c) 2015 by Rafael Angel Aznar Aparici (rafaaznar at gmail dot com)
 * 
 * openAUSIAS: The stunning micro-library that helps you to develop easily 
 * AJAX web applications by using Java and jQuery
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
 * 
 */
package net.daw.service.specific.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import net.daw.bean.specific.implementation.ProfesorBean;
import net.daw.connection.implementation.BoneConnectionPoolImpl;
import net.daw.dao.specific.implementation.ProfesorDao;
import net.daw.helper.statics.FilterBeanHelper;
import net.daw.helper.statics.ParameterCook;
import net.daw.service.generic.implementation.TableServiceGenImpl;

/**
 *
 * @author a022583461z
 */
public class ProfesorService extends TableServiceGenImpl {

    public ProfesorService(HttpServletRequest request) {
        super(request);
    }

    //recibo URL
    //El controlador ha visto la operacion
    // le paso pojo con id
    @Override
    public String get() throws Exception {

        int id = ParameterCook.prepareId(oRequest);
        Connection oConnection = null;
        oConnection = new BoneConnectionPoolImpl().newConnection();
        //lo que hay por debajo hay que crearlo        
        ProfesorDao oProfesorDao = new ProfesorDao(oConnection);
        ProfesorBean oProfesorBean = new ProfesorBean();
        oProfesorBean.setId(id);
        oProfesorDao.get(oProfesorBean, 1);
        //convertimos pojo en json
        Gson oGson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        return oGson.toJson(oProfesorBean);

    }

    @Override
    public String getall() throws Exception {

        ArrayList<FilterBeanHelper> alFilterBeanHelper = ParameterCook.prepareFilter(oRequest);
        HashMap<String, String> hmOrder = ParameterCook.prepareOrder(oRequest);
        Connection oConnection = new BoneConnectionPoolImpl().newConnection();
        ProfesorDao oProfesorDao = new ProfesorDao(oConnection);
        ArrayList<ProfesorBean> alProfesores = new ArrayList<>();
        alProfesores = oProfesorDao.getAll(alFilterBeanHelper, hmOrder);

        Gson oGson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        return oGson.toJson(alProfesores);

    }

    @Override
    public String getcount() throws Exception {
        Connection oConnection = new BoneConnectionPoolImpl().newConnection();
        ProfesorDao oProfesorDao = new ProfesorDao(oConnection);
        ArrayList<FilterBeanHelper> alFilterBeanHelper = ParameterCook.prepareFilter(oRequest);
        int count = oProfesorDao.getCount(alFilterBeanHelper);

        Gson oGson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

        return "{\"status\":200,\"message\":" + oGson.toJson(count) + "}";
    }

    @Override
    public String getpage() throws Exception {
        Connection oConnection = new BoneConnectionPoolImpl().newConnection();
        ProfesorDao oProfesorDao = new ProfesorDao(oConnection);
        int IntPage = ParameterCook.preparePage(oRequest);
        int intRpp = ParameterCook.prepareRpp(oRequest);
        //
        ArrayList<FilterBeanHelper> alFilterBeanHelper = ParameterCook.prepareFilter(oRequest);
        HashMap<String, String> hmOrder = ParameterCook.prepareOrder(oRequest);

        ArrayList<ProfesorBean> alProfesorBean = oProfesorDao.getPage(intRpp, IntPage, alFilterBeanHelper, hmOrder);
        Gson oGson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        return oGson.toJson(alProfesorBean);

    }

    @Override
    public String getpages() throws Exception {
        Connection oConnection = new BoneConnectionPoolImpl().newConnection();
        ProfesorDao oProfesorDao = new ProfesorDao(oConnection);

        int intRpp = ParameterCook.prepareRpp(oRequest);
        ArrayList<FilterBeanHelper> alFilterBeanHelper = ParameterCook.prepareFilter(oRequest);

        return ((Integer) oProfesorDao.getPages(intRpp, alFilterBeanHelper)).toString();
    }
}
