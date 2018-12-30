package me.david.davidlib.gamesngine.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.david.davidlib.gamesngine.util.BufferUtil;
import me.david.davidlib.math.Vector2D;
import me.david.davidlib.math.Vector3D;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.*;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModelLoader {

    private static ModelLoader instance = new ModelLoader();

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public RawModel loadToVAO(double[] positions, double[] textureCoords, double[] normals, int[] indices){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        unbindVAO();
        return new RawModel(vaoID,indices.length);
    }

    public int loadTexture(String fileName) {
        Texture modelTexture = null;
        try {
            modelTexture = TextureLoader.getTexture("PNG", new FileInputStream("textures/" + fileName + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int id = modelTexture.getTextureID();
        textures.add(id);
        return id;
    }

    public void deleteAll() {
        vaos.forEach(GL30::glDeleteVertexArrays);
        vbos.forEach(GL15::glDeleteBuffers);
        textures.forEach(GL11::glDeleteTextures);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, double[] data) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        DoubleBuffer buffer = BufferUtil.createBuffer(data, true);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, coordinateSize, GL_DOUBLE,false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO(){
        glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = BufferUtil.createBuffer(indices, true);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    public RawModel loadObjModel(String fileName) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(new File("objects/" + fileName + ".obj")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        String line;
        List<Vector3D> vertices = new ArrayList<>();
        List<Vector2D> textures = new ArrayList<>();
        List<Vector3D> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        double[] verticesArray;
        double[] normalsArray = null;
        double[] textureArray = null;
        int[] indicesArray;
        try {

            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3D vertex = new Vector3D(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) {
                    Vector2D texture = new Vector2D(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    Vector3D normal = new Vector3D(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    textureArray = new double[vertices.size() * 2];
                    normalsArray = new double[vertices.size() * 3];
                    break;
                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1,indices,textures,normals,textureArray,normalsArray);
                processVertex(vertex2,indices,textures,normals,textureArray,normalsArray);
                processVertex(vertex3,indices,textures,normals,textureArray,normalsArray);
                line = reader.readLine();
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        verticesArray = new double[vertices.size()*3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for (Vector3D vertex: vertices) {
            verticesArray[vertexPointer++] = vertex.getX();
            verticesArray[vertexPointer++] = vertex.getY();
            verticesArray[vertexPointer++] = vertex.getZ();
        }

        for(int i=0;i<indices.size();i++){
            indicesArray[i] = indices.get(i);
        }
        return loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);

    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2D> textures, List<Vector3D> normals, double[] textureArray, double[] normalsArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);
        Vector2D currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
        textureArray[currentVertexPointer*2] = currentTex.getX();
        textureArray[currentVertexPointer*2+1] = 1 - currentTex.getY();
        Vector3D currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
        normalsArray[currentVertexPointer*3] = currentNorm.getX();
        normalsArray[currentVertexPointer*3+1] = currentNorm.getY();
        normalsArray[currentVertexPointer*3+2] = currentNorm.getZ();
    }

}
