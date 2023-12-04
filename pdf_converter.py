import PyPDF2

def pdf_to_text(pdf_path : str) -> str:
    # read the pdf file
    pdf_reader = PyPDF2.PdfReader(open(f'{pdf_path}', 'rb'))
    
    text = ''
    pdf_pages = len(pdf_reader.pages)
    for i in range(pdf_pages):
        page = pdf_reader.pages[i].extract_text()
        text = text + page + '\n'    
    
    return text

# print(pdf_to_text('2.pdf'))
    
    
    