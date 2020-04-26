package bg.sofia.uni.fmi.mjt.authroship.detection.exceptios;

public class CalculatingSignatureException extends Exception {
    @Override
    public String getMessage() {
        return "Problem creating linguistic signature from this stream.";
    }
}
