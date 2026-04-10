import csv
import random
import os

input_file = r'd:\Msc\AdvSoftArch\Code\msc_eLib\backend\ElibMain\SearchService\src\main\resources\books.csv'
output_file = r'd:\Msc\AdvSoftArch\Code\msc_eLib\backend\ElibMain\SearchService\src\main\resources\books_updated.csv'

languages = ['english', 'spanish', 'french', 'multi']
lang_weights = [85, 5, 5, 5]

ages = ['12+', '16+', '18+', '21+']
age_weights = [70, 15, 10, 5]

with open(input_file, mode='r', encoding='utf-8') as infile, \
     open(output_file, mode='w', encoding='utf-8', newline='') as outfile:
     
    reader = csv.reader(infile)
    writer = csv.writer(outfile, quoting=csv.QUOTE_MINIMAL)
    
    header = next(reader)
    header.extend(['language', 'age'])
    writer.writerow(header)
    
    for row in reader:
        lang = random.choices(languages, weights=lang_weights)[0]
        age = random.choices(ages, weights=age_weights)[0]
        
        row.extend([lang, age])
        writer.writerow(row)

# Replace the internal file
os.replace(output_file, input_file)
print(f"Update complete. Added mock 'language' and 'age' columns to {input_file}.")
