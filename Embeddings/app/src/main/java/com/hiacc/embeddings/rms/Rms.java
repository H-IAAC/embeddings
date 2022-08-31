package com.hiacc.embeddings.rms;

public class Rms {
    public static float rmsValue(float[] x) {
        if (x.length == 0)
            return 0.0F;

        float rms = 0.0F;
        for (float i : x)
            rms += i * i;
        rms /= x.length;
        return (float) Math.sqrt(rms);
    }
}
