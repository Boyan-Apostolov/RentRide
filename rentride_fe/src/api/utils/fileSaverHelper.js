export const fileToBase64 = async (file) => {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve("data:image/png;base64, " + reader.result.split(',')[1]);
        reader.onerror = (error) => reject(error);
    });
}