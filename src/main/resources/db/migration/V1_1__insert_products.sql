insert into product
    (id, full_name, full_description, image_path,
     price, short_description, city,
     country, order_id, available, posted_on)
     values (1, 'Gaming Laptop', 'long description', '1_laptop.jpg',
             2300.0, 'Elegant and powerful gaming laptop. Has very cool screen',
             'Krakow', 'Poland', null, true, STR_TO_DATE('01,5,2013', '%d,%m,%Y'));

insert into product
(id, full_name, full_description, image_path,
 price, short_description, city,
 country, order_id, available, posted_on)
values (2, 'Headphones Super-Photo 2980', 'long description', '2_head_phones.jpg',
        2300.0, 'Headphones with really loud bass. Your neighbours can listen with you',
        'New York', 'USA', null, true, STR_TO_DATE('01,5,2014', '%d,%m,%Y'));

insert into product
(id, full_name, full_description, image_path,
 price, short_description, city,
 country, order_id, available, posted_on)
values (3, 'Random ancient statue', 'long description', '3_statue.jpg',
        9990.0, 'I have just found it in my garden lol, but it definitely belongs to some ancient greeks or something',
        'Munich', 'Germany', null, true, STR_TO_DATE('01,5,2015', '%d,%m,%Y'));

insert into product
(id, full_name, full_description, image_path,
 price, short_description, city,
 country, order_id, available, posted_on)
values (4, 'A math book', 'long description', '5_book.jpg',
        50.0, 'Learn algebra, calculus and some other strange stuff',
        'Lyon',  'France', null, true, STR_TO_DATE('01,5,2016', '%d,%m,%Y'));

insert into product
(id, full_name, full_description, image_path,
 price, short_description, city,
 country, order_id, available, posted_on)
values (5, 'Sport shoes. Good for jumping', 'long description', '4_shoes.jpg',
        230.0, 'You can not only walk in those boots bu also you can jump! That\'s unbelievable!',
        'Stockholm', 'Sweden', null, true, STR_TO_DATE('01,5,2021', '%d,%m,%Y'));
