package group_0522.csc207.gamecentre.Common;

import android.support.v7.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static android.content.Context.MODE_PRIVATE;

/**
 * FileConverter to save and load file
 */
public class FileConverter {
    /**
     * The current activity
     */
    private final AppCompatActivity activity;

    /**
     * Create a new FileConverter
     *
     * @param Activity the current activity
     */
    public FileConverter(AppCompatActivity Activity) {
        this.activity = Activity;
    }

    /**
     * Return the Object at the given directory
     *
     * @param fileDir the file directory
     * @return The Object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object loadFromFile(String fileDir) throws IOException, ClassNotFoundException {
        Object result = null;

        try {
            InputStream inputStream = activity.openFileInput(fileDir);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                result = input.readObject();
                inputStream.close();
            }

        } catch (FileNotFoundException e) {
        }
        return result;

    }


    /**
     * Save the object to fileName.
     *
     * @param fileName the name for the file
     * @param object
     * @throws IOException
     */
    public void saveToFile(String fileName, Object object) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(
                activity.openFileOutput(fileName, MODE_PRIVATE));
        outputStream.writeObject(object);
        outputStream.close();
    }
}