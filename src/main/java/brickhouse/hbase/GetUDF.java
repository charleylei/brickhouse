package brickhouse.hbase;
/**
 * Copyright 2012 Klout, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.Map;

/**
 * Simple UDF for doing single PUT into HBase table ..
 * Not intended for doing massive reads from HBase,
 * but only when relatively few rows are being read.
 */
@Description(name = "hbase_get",
        value = "_FUNC_(table,key,family) - Do a single HBase Get on a table "
)
public class GetUDF extends UDF {


    public String evaluate(Map<String, String> config, String key) {
        try {
            HTable table = HTableFactory.getHTable(config);
            Get theGet = new Get(key.getBytes());
            Result res = table.get(theGet);

            byte[] valBytes = res.getValue(config.get(HTableFactory.FAMILY_TAG).getBytes(), config.get(HTableFactory.QUALIFIER_TAG).getBytes());
            if (valBytes != null) {
                return new String(valBytes);
            }
            return null;
        } catch (Exception exc) {
            ///LOG.error(" Error while trying HBase PUT ",exc);
            throw new RuntimeException(exc);
        }
    }

}
