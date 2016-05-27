package jp.ac.titech.itpro.sdl.gles10ex;

import android.renderscript.Float3;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by WL-Amigo on 16/05/27.
 */
public class Cone implements SimpleRenderer.Obj {
    private FloatBuffer vbuf;
    private float x, y, z;
    private int numSplit;
    private ArrayList<Float> vertices;

    public Cone(float x, float y, float z, float h, float r, int numSplit) {
        this.numSplit = numSplit;
        this.vertices = new ArrayList<Float>();
        makeVerticesArrayList(this.vertices, h, r, numSplit);
        Log.d("Cone", "Cone: vertices.size() : " + this.vertices.size());

        float[] verticesArray = new float[vertices.size()];
        int i = 0;
        for(Float elem : vertices){
            verticesArray[i++] = elem != null ? elem : Float.NaN;
        }
        vbuf = ByteBuffer.allocateDirect(verticesArray.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vbuf.put(verticesArray);
        vbuf.position(0);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private void makeVerticesArrayList(ArrayList<Float> dest, float h, float r, int numSplit){
        for(int i = 0; i < numSplit; i++){
            float x1, y1, x2, y2;
            x1 = (float)Math.cos(Math.PI * 2.0 / (double)numSplit * (double)i) * r;
            y1 = (float)Math.sin(Math.PI * 2.0 / (double)numSplit * (double)i) * r;
            x2 = (float)Math.cos(Math.PI * 2.0 / (double)numSplit * (double)(i + 1)) * r;
            y2 = (float)Math.sin(Math.PI * 2.0 / (double)numSplit * (double)(i + 1)) * r;

            dest.add(0.0F); dest.add(0.0F); dest.add(h);
            dest.add(x1); dest.add(y1); dest.add(0.0F);
            dest.add(x2); dest.add(y2); dest.add(0.0F);
        }
    }

    public void draw(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vbuf);

        for(int i = 0; i < numSplit; i++) {
            // calcurate normal vector
            Float3 nv = calcurateNormal(new Float3(vertices.get((i * 3 + 1) * 3), vertices.get((i * 3 + 1) * 3 + 1), -vertices.get(i * 9 + 2)),
                    new Float3(vertices.get((i * 3 + 2) * 3), vertices.get((i * 3 + 2) * 3 + 1), -vertices.get(i * 9 + 2)));
            gl.glNormal3f(nv.x, nv.y, nv.z);
            gl.glDrawArrays(GL10.GL_TRIANGLES, i * 3, 3);
        }
    }

    private Float3 calcurateNormal(Float3 v1, Float3 v2){
        float nvx, nvy, nvz, distance;
        nvx = v1.y * v2.z - v1.z * v2.y;
        nvy = v1.z * v2.x - v1.x * v2.z;
        nvz = v1.x * v2.y - v1.y * v2.x;
        distance = (float)Math.sqrt(nvx * nvx + nvy * nvy + nvz * nvz);
        nvx /= distance;
        nvy /= distance;
        nvz /= distance;
        return new Float3(nvx, nvy, nvz);
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getZ() {
        return z;
    }

}
