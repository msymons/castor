/*
 * Copyright 2006 Le Duc Bao, Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.ddlgen.typeinfo;

import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.GeneratorException;
import org.castor.ddlgen.schemaobject.Field;

/**
 * Final TypeInfo for types having a required length parameter.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.1
 */
public final class RequiredLengthType extends AbstractType {
    //--------------------------------------------------------------------------

    /** Default length parameter from ddl.properties file. Will be used if no specific
     *  length is specified at field mapping. */
    private final Integer _defaultLength;
    
    /** Post fix to be appended after type. */
    private final String _postfix;
    
    //--------------------------------------------------------------------------

    /**
     * Construct a new TypeInfo instance with given JDBC type, SQL type and Configuration.
     * 
     * @param jdbcType The JDBC type.
     * @param sqlType The SQL type.
     * @param conf The configuration to get default parameter values from.
     */
    public RequiredLengthType(final String jdbcType, final String sqlType,
                              final DDLGenConfiguration conf) {
        this(jdbcType, sqlType, "", conf);
    }

    /**
     * Construct a new TypeInfo instance with given JDBC type, SQL type and Configuration.
     * 
     * @param jdbcType The JDBC type.
     * @param sqlType The SQL type.
     * @param postfix Post fix to be appended after type.
     * @param conf The configuration to get default parameter values from.
     */
    public RequiredLengthType(final String jdbcType, final String sqlType,
                              final String postfix, final DDLGenConfiguration conf) {
        super(jdbcType, sqlType);

        String param = PARAM_PREFIX + jdbcType + PARAM_POSTFIX_LENGTH;
        _defaultLength = conf.getInteger(param);
        _postfix = postfix;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String toDDL(final Field field) throws GeneratorException {
        Integer length = field.getLength();
        if (length == null) { length = _defaultLength; }
        if (length == null) {
            throw new GeneratorException(
                    "Reguired length attribute missing for field '" + field.getName()
                    + "' of type '" + getJdbcType() + "'");
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append(getSqlType());
        sb.append('(').append(length).append(')');
        sb.append(_postfix);
        
        return sb.toString();
    }

    //--------------------------------------------------------------------------
}
