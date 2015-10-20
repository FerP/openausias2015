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
package net.daw.dao.specific.implementation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import net.daw.bean.specific.implementation.ProfesorBean;

import net.daw.dao.generic.implementation.TableDaoGenImpl;
import net.daw.data.specific.implementation.MysqlDataSpImpl;
import net.daw.helper.statics.FilterBeanHelper;
import net.daw.helper.statics.SqlBuilder;

/**
 *
 * @author 22583461z
 */
public class ProfesorDao extends TableDaoGenImpl<ProfesorBean> {

    public ProfesorDao(Connection pooledConnection) throws Exception {
        super(pooledConnection);
    }

    @Override //Con el Override,machaco la operacion get original de lo generico
    public ProfesorBean get(ProfesorBean oProfesorBean, Integer expand) throws Exception {
        MysqlDataSpImpl oMySQL = new MysqlDataSpImpl(oConnection);
        if (oProfesorBean.getId() > 0) {
            if (oMySQL.existsOne(strSqlSelectDataOrigin, oProfesorBean.getId())) {

                oProfesorBean.setNombre(oMySQL.getOne(strSqlSelectDataOrigin, "nombre", oProfesorBean.getId()));
                oProfesorBean.setEstado(oMySQL.getOne(strSqlSelectDataOrigin, "estado", oProfesorBean.getId()));

            }
        }

//
//        oProfesorBean.setId(23);//Al pasar por aqui, siempre coje el 23, le pida lo que le pida
//        oProfesorBean.setNombre("Jose Ramon");
//        oProfesorBean.setEstado("Tranquilo");
        return oProfesorBean;

    }

    @Override
    public ArrayList<ProfesorBean> getAll(ArrayList<FilterBeanHelper> alFilter, HashMap<String, String> hmOrder) throws Exception {
        strSqlSelectDataOrigin+=SqlBuilder.buildSqlWhere(alFilter);
         strSqlSelectDataOrigin+=SqlBuilder.buildSqlOrder(hmOrder);
        ResultSet oResultset = oMysql.getAllSql(strSqlSelectDataOrigin);
        
        ArrayList<ProfesorBean> alProfesores = new ArrayList();
        while (oResultset.next()) {
            int id = oResultset.getInt("id");

            ProfesorBean oProfesor = new ProfesorBean();
            oProfesor.setNombre(oMysql.getOne(strSqlSelectDataOrigin, "nombre", id));
            oProfesor.setEstado(oMysql.getOne(strSqlSelectDataOrigin, "estado", id));
            alProfesores.add(oProfesor);

        }

        return alProfesores;

    }

    @Override
    public int getCount(ArrayList<FilterBeanHelper> alFilter) throws Exception {
        return oMysql.getCount(strSqlSelectDataOrigin);
    }

    @Override
    public ArrayList<ProfesorBean> getPage(int intRegsPerPag, int intPage, ArrayList<FilterBeanHelper> alFilter, HashMap<String, String> hmOrder) throws Exception {
        strSqlSelectDataOrigin+=SqlBuilder.buildSqlWhere(alFilter);
         strSqlSelectDataOrigin+=SqlBuilder.buildSqlOrder(hmOrder);
        ResultSet oResultset = oMysql.getAllSql(strSqlSelectDataOrigin);
        ArrayList<ProfesorBean> alProfesores = new ArrayList();
        while (oResultset.next()) {
            int id = oResultset.getInt("id");

            ProfesorBean oProfesor = new ProfesorBean();
            oProfesor.setNombre(oMysql.getOne(strSqlSelectDataOrigin, "nombre", id));
            oProfesor.setEstado(oMysql.getOne(strSqlSelectDataOrigin, "estado", id));
            alProfesores.add(oProfesor);

        }

        return alProfesores;

    }
    
    @Override
     public int getPages(int intRegsPerPag, ArrayList<FilterBeanHelper> alFilter) throws Exception{
         return oMysql.getPages(strSqlSelectDataOrigin,intRegsPerPag);
         
     }
    
    
}
