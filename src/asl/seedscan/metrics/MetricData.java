/*
 * Copyright 2012, United States Geological Survey or
 * third-party contributors as indicated by the @author tags.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/  >.
 *
 */
package asl.seedscan.metrics;

import asl.metadata.Channel;
import asl.metadata.ChannelArray;
import asl.metadata.meta_new.StationMeta;
import asl.metadata.meta_new.ChannelMeta;
import asl.seedsplitter.DataSet;
import asl.security.MemberDigest;
import asl.util.Hex;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.logging.Logger;

public class MetricData
{
    private static final Logger logger = Logger.getLogger("asl.seedscan.metrics.MetricData");

    private Hashtable<String, ArrayList<DataSet>> data;
    //private Hashtable<String, StationData> metadata;
    private StationMeta metadata;
    private Hashtable<String, String> synthetics;

  //constructor(s)
    public MetricData(Hashtable<String, ArrayList<DataSet>> data, 
                      StationMeta metadata,
                      Hashtable<String, String> synthetics)
    {
        this.data = data;
        this.metadata = metadata;
        this.synthetics = synthetics;
    }

    public MetricData(Hashtable<String, ArrayList<DataSet>> data, 
                      StationMeta metadata)
    {
        this.data = data;
        this.metadata = metadata;
    }

    public MetricData(Hashtable<String, ArrayList<DataSet>> data)
    {
        this.data = data;
    }

    public StationMeta getMetaData()
    {
        return metadata;
    }

  // getChannelData()
  // Returns ArrayList<DataSet> = All DataSets for a given channel (e.g., "00-BHZ")

    public ArrayList<DataSet> getChannelData(String location, String name)
    {
        String locationName = location + "-" + name;
        Set<String> keys = data.keySet();
        for (String key : keys){          // key looks like "IU_ANMO 00-BHZ (20.0 Hz)"
           if (key.contains(locationName) ){
            //System.out.format(" key=%s contains locationName=%s\n", key, locationName);
              return data.get(key);       // return ArrayList<DataSet>
           }
        }
        return null;           
    }

    public ArrayList<DataSet> getChannelData(Channel channel)
    {
        return getChannelData(channel.getLocation(), channel.getChannel() );           
    }


 // byteArray is used to pass the digest (as byte array) back to the metric
    public Boolean hashChanged(Channel channel, byte[] byteArray)
    {
        ChannelArray channelArray = new ChannelArray(channel.getLocation(), channel.getChannel());
        return hashChanged(channelArray, byteArray);
    }

    public Boolean hashChanged(Channel channelA, Channel channelB, byte[] byteArray)
    {
        ChannelArray channelArray = new ChannelArray(channelA, channelB);
        return hashChanged(channelArray, byteArray);
    }

    public Boolean hashChanged(ChannelArray channelArray, byte[] byteArray)
    {
        ArrayList<ByteBuffer> digests = new ArrayList<ByteBuffer>();

        ArrayList<Channel> channels = channelArray.getChannels();
        for (Channel channel : channels){
            ChannelMeta chanMeta  = getMetaData().getChanMeta(channel);
            if (chanMeta == null){
                System.out.format("MetricData.hashChanged() Error: metadata not found for requested channel:%s\n",channel);
                return false;
            }
            else {
                digests.add(chanMeta.getDigestBytes());
            }

            ArrayList<DataSet>datasets = getChannelData(channel);
            if (datasets == null){
                System.out.format("MetricData.hashChanged() Error: Data not found for requested channel:%s\n",channel);
                return false;
            }
            else {
                digests.add(datasets.get(0).getDigestBytes());
            }
        }
        ByteBuffer digest = MemberDigest.multiBuffer(digests);
        digest.clear();
        digest.get(byteArray, 0, byteArray.length);

        //String multiDigestString = Hex.byteArrayToHexString(digest.array());
        //System.out.format("== Multi DataDigest string=%s\n", multiDigestString);

      // Here's where we need to check this digest against a stored value
      // If the digest hasn't changed then return false

        return true;
    }

}
