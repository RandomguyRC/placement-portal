import os
from bs4 import BeautifulSoup
from tkinter import Tk, filedialog

# CHANGE THIS TO YOUR OUTPUT FOLDER
OUTPUT_FOLDER = r"C:\Users\jastr\OneDrive\Desktop\myfiles\College\Sem - 5\DBMS Project\Placement Portal\placement-website\sep\sep webpages"

def split_html_css(input_file, output_folder):
    # Read the input HTML file
    with open(input_file, "r", encoding="utf-8") as f:
        html_content = f.read()

    # Parse HTML
    soup = BeautifulSoup(html_content, "html.parser")

    # Extract <style> content
    styles = soup.find_all("style")
    css_content = ""
    for style in styles:
        css_content += style.get_text() + "\n"
        style.decompose()  # remove the <style> tag

    # File names
    base_name = os.path.splitext(os.path.basename(input_file))[0]
    output_html = os.path.join(output_folder, f"{base_name}_clean.html")
    output_css = os.path.join(output_folder, f"{base_name}.css")

    # Ensure output folder exists
    os.makedirs(output_folder, exist_ok=True)

    # Add <link> to CSS file in <head>
    head = soup.head
    if head:
        link_tag = soup.new_tag("link", rel="stylesheet", href=os.path.basename(output_css))
        head.append(link_tag)

    # Save the modified HTML
    with open(output_html, "w", encoding="utf-8") as f:
        f.write(str(soup))

    # Save the extracted CSS
    if css_content.strip():
        with open(output_css, "w", encoding="utf-8") as f:
            f.write(css_content)

    print(f"✅ Done! Created:\n- {output_html}\n- {output_css}")

if __name__ == "__main__":
    # Hide the main tkinter window
    Tk().withdraw()

    # Ask the user to select an HTML file
    input_file = filedialog.askopenfilename(
        title="Select an HTML file",
        filetypes=[("HTML Files", "*.html *.htm")]
    )

    if not input_file:
        print("❌ No file selected. Exiting.")
    else:
        split_html_css(input_file, OUTPUT_FOLDER)
