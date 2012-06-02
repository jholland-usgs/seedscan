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
package asl.seedscan.scan;

import java.util.ArrayList;

public class Scan
{
    private String pathPattern;
    private int startDay;
    private int daysToScan;
    private ArrayList<ScanOperation> operations;

    // TODO: add methods for managing inclusions/exclusions
    private Filter networks;
    private Filter stations;
    private Filter locations;
    private Filter channels;

    public Scan()
    {
        operations = new ArrayList<ScanOperation>();
    }

 // path pattern
    public void setPathPattern(String pathPattern)
    {
        this.pathPattern = pathPattern;
    }

    public String getPathPattern()
    {
        return pathPattern;
    }

 // operations
    public void addOperation(ScanOperation operation)
    {
        operations.add(operation);
    }

    public ScanOperation getOperation(int index)
    throws IndexOutOfBoundsException
    {
        return operations.get(index);
    }

    public ArrayList<ScanOperation> getOperations()
    {
        return operations;
    }

    public boolean removeOperation(ScanOperation operation)
    {
        return operations.remove(operation);
    }

    public ScanOperation removeOperations(int index)
    throws IndexOutOfBoundsException
    {
        return operations.remove(index);
    }

 // start depth
    public void setStartDay(int startDay)
    {
        this.startDay = startDay;
    }

    public int getStartDay()
    {
        return startDay;
    }

 // scan depth
   public void setDaysToScan(int daysToScan)
   {
       this.daysToScan = daysToScan;
   }

   public int getDaysToScan()
   {
       return daysToScan;
   }
}
