package liqueurPlant.client.silo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.model.ResourceModel.Type;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;

    public  class DeviceEnabler extends BaseInstanceEnabler {

        public DeviceEnabler() {
        	System.out.println("Device constructor ran");
            // notify new date each 5 second
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    fireResourcesChange(13);
                }
            }, 5000, 5000);
        }

        @Override
        public ReadResponse read(int resourceid) {
            System.out.println("Read on Device Resource " + resourceid);
            switch (resourceid) {
            case 0:
                return ReadResponse.success(resourceid, getManufacturer());
            case 1:
                return ReadResponse.success(resourceid, getModelNumber());
            case 2:
                return ReadResponse.success(resourceid, getSerialNumber());
            case 3:
                return ReadResponse.success(resourceid, getFirmwareVersion());
            case 9:
                return ReadResponse.success(resourceid, getBatteryLevel());
            case 10:
                return ReadResponse.success(resourceid, getMemoryFree());
            case 11:
                Map<Integer, Long> errorCodes = new HashMap<>();
                errorCodes.put(0, getErrorCode());
                return ReadResponse.success(resourceid, errorCodes, Type.INTEGER);
            case 13:
                return ReadResponse.success(resourceid, getCurrentTime());
            case 14:
                return ReadResponse.success(resourceid, getUtcOffset());
            case 15:
                return ReadResponse.success(resourceid, getTimezone());
            case 16:
                return ReadResponse.success(resourceid, getSupportedBinding());
            default:
                return super.read(resourceid);
            }
        }

        @Override
        public ExecuteResponse execute(int resourceid, String params) {
            System.out.println("Execute on Device resource " + resourceid);
            if (params != null && params.length() != 0)
                System.out.println("\t params " + params);
            return ExecuteResponse.success();
        }

        @Override
        public WriteResponse write(int resourceid, LwM2mResource value) {
            System.out.println("Write on Device Resource " + resourceid + " value " + value);
            switch (resourceid) {
            case 13:
                return WriteResponse.notFound();
            case 14:
                setUtcOffset((String) value.getValue());
                fireResourcesChange(resourceid);
                return WriteResponse.success();
            case 15:
                setTimezone((String) value.getValue());
                fireResourcesChange(resourceid);
                return WriteResponse.success();
            default:
                return super.write(resourceid, value);
            }
        }

        private String getManufacturer() {
            return "Leshan Example Device";
        }

        private String getModelNumber() {
            return "Model 500";
        }

        private String getSerialNumber() {
            return "LT-500-000-0001";
        }

        private long getErrorCode() {
            return 0;
        }
        
        private String getFirmwareVersion() {
            return "1.0.0";
        }

        private int getBatteryLevel() {
            final Random rand = new Random();
            return rand.nextInt(100);
        }

        private int getMemoryFree() {
            final Random rand = new Random();
            return rand.nextInt(50) + 114;
        }

        private Date getCurrentTime() {
            return new Date();
        }

        private String utcOffset = new SimpleDateFormat("X").format(Calendar.getInstance().getTime());;

        private String getUtcOffset() {
            return utcOffset;
        }

        private void setUtcOffset(String t) {
            utcOffset = t;
        }

        private String timeZone = TimeZone.getDefault().getID();

        private String getTimezone() {
            return timeZone;
        }

        private void setTimezone(String t) {
            timeZone = t;
        }

        private String getSupportedBinding() {
            return "U";
        }
    }
